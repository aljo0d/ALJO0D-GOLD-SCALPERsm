//+------------------------------------------------------------------+
//| ALJO0D GOLD SCALPER CONNECTOR                                    |
//| MT5 Demo Connector                                                |
//|                                                                  |
//| Purpose: Connect MT5 to the ALJO0D Trading Engine                |
//| IMPORTANT: Demo testing only                                    |
//+------------------------------------------------------------------+
#property strict
#property version   "1.0"

#include <Trade/Trade.mqh>

CTrade trade;

//-------------------------------------------------------------------
// SETTINGS
//-------------------------------------------------------------------
input string ApiBaseUrl = "https://aljo0d-gold-scalpersm-production.up.railway.app";
input int    PollSeconds = 2;
input bool   DemoOnly = true;

// Safety: this must remain false for now.
input bool   AllowRealTrading = false;

//-------------------------------------------------------------------
// STATE
//-------------------------------------------------------------------
bool LastRunning = false;
datetime LastPoll = 0;

//-------------------------------------------------------------------
// URL ENCODING
//-------------------------------------------------------------------
string UrlEncode(string text)
{
   string result = "";

   for(int i = 0; i < StringLen(text); i++)
   {
      ushort c = StringGetCharacter(text, i);

      if(
         (c >= 'a' && c <= 'z') ||
         (c >= 'A' && c <= 'Z') ||
         (c >= '0' && c <= '9') ||
         c == '-' ||
         c == '_' ||
         c == '.' ||
         c == '~'
      )
      {
         result += ShortToString((short)c);
      }
      else
      {
         result += StringFormat("%%%02X", c);
      }
   }

   return result;
}

//-------------------------------------------------------------------
// HTTP GET
//-------------------------------------------------------------------
bool HttpGet(string endpoint, string &response)
{
   string url = ApiBaseUrl + endpoint;

   char data[];
   char result[];
   string headers;

   ResetLastError();

   int status = WebRequest(
      "GET",
      url,
      "",
      "",
      15000,
      data,
      0,
      result,
      headers
   );

   if(status == -1)
   {
      Print("WebRequest failed. Error: ", GetLastError());
      return false;
   }

   response = CharArrayToString(result);

   Print("GET ", endpoint, " -> HTTP ", status);

   return (status >= 200 && status < 300);
}

//-------------------------------------------------------------------
// HTTP POST
//-------------------------------------------------------------------
bool HttpPost(string endpoint, string json, string &response)
{
   string url = ApiBaseUrl + endpoint;

   char data[];
   char result[];
   string headers;

   StringToCharArray(
      json,
      data,
      0,
      StringLen(json),
      CP_UTF8
   );

   ResetLastError();

   int status = WebRequest(
      "POST",
      url,
      "Content-Type: application/json\r\n",
      "",
      15000,
      data,
      ArraySize(data) - 1,
      result,
      headers
   );

   if(status == -1)
   {
      Print("WebRequest failed. Error: ", GetLastError());
      return false;
   }

   response = CharArrayToString(result);

   Print("POST ", endpoint, " -> HTTP ", status);

   return (status >= 200 && status < 300);
}

//-------------------------------------------------------------------
// JSON VALUE - STRING
//-------------------------------------------------------------------
string JsonString(
   string json,
   string key,
   string defaultValue = ""
)
{
   string search = "\"" + key + "\":\"";

   int start = StringFind(json, search);

   if(start < 0)
      return defaultValue;

   start += StringLen(search);

   int end = StringFind(json, "\"", start);

   if(end < 0)
      return defaultValue;

   return StringSubstr(
      json,
      start,
      end - start
   );
}

//-------------------------------------------------------------------
// JSON VALUE - BOOL
//-------------------------------------------------------------------
bool JsonBool(
   string json,
   string key,
   bool defaultValue = false
)
{
   string search = "\"" + key + "\":";

   int start = StringFind(json, search);

   if(start < 0)
      return defaultValue;

   start += StringLen(search);

   string value = StringSubstr(json, start, 10);

   if(StringFind(value, "true") == 0)
      return true;

   if(StringFind(value, "false") == 0)
      return false;

   return defaultValue;
}

//-------------------------------------------------------------------
// CHECK ACCOUNT
//-------------------------------------------------------------------
bool IsDemoAccount()
{
   long mode = AccountInfoInteger(ACCOUNT_TRADE_MODE);

   return (mode == ACCOUNT_TRADE_MODE_DEMO);
}

//-------------------------------------------------------------------
// SYMBOL
//-------------------------------------------------------------------
string FindGoldSymbol()
{
   if(SymbolSelect("XAUUSD", true))
      return "XAUUSD";

   if(SymbolSelect("XAUUSDm", true))
      return "XAUUSDm";

   if(SymbolSelect("GOLD", true))
      return "GOLD";

   return _Symbol;
}

//-------------------------------------------------------------------
// NORMALIZE LOT
//-------------------------------------------------------------------
double NormalizeLot(string lotText)
{
   double minLot =
      SymbolInfoDouble(
         _Symbol,
         SYMBOL_VOLUME_MIN
      );

   double maxLot =
      SymbolInfoDouble(
         _Symbol,
         SYMBOL_VOLUME_MAX
      );

   double step =
      SymbolInfoDouble(
         _Symbol,
         SYMBOL_VOLUME_STEP
      );

   double lot = minLot;

   if(lotText != "Minimum")
   {
      double requested =
         StringToDouble(lotText);

      if(requested > 0)
         lot = requested;
   }

   if(lot < minLot)
      lot = minLot;

   if(lot > maxLot)
      lot = maxLot;

   if(step > 0)
      lot =
         MathFloor(
            lot / step
         ) * step;

   return NormalizeDouble(
      lot,
      2
   );
}

//-------------------------------------------------------------------
// COUNT OUR POSITIONS
//-------------------------------------------------------------------
int CountOurPositions()
{
   int count = 0;

   for(int i = 0; i < PositionsTotal(); i++)
   {
      ulong ticket =
         PositionGetTicket(i);

      if(ticket == 0)
         continue;

      if(!PositionSelectByTicket(ticket))
         continue;

      string symbol =
         PositionGetString(
            POSITION_SYMBOL
         );

      if(symbol == _Symbol)
         count++;
   }

   return count;
}

//-------------------------------------------------------------------
// CLOSE OUR POSITIONS
//-------------------------------------------------------------------
void CloseOurPositions()
{
   for(int i = PositionsTotal() - 1; i >= 0; i--)
   {
      ulong ticket =
         PositionGetTicket(i);

      if(ticket == 0)
         continue;

      if(!PositionSelectByTicket(ticket))
         continue;

      string symbol =
         PositionGetString(
            POSITION_SYMBOL
         );

      if(symbol != _Symbol)
         continue;

      if(!trade.PositionClose(ticket))
      {
         Print(
            "Failed to close position ",
            ticket,
            " Error=",
            GetLastError()
         );
      }
   }
}

//-------------------------------------------------------------------
// OPEN DEMO TEST TRADE
//-------------------------------------------------------------------
bool OpenDemoTestTrade(
   string lotText,
   string stopLossText,
   string takeProfitText
)
{
   if(DemoOnly && !IsDemoAccount())
   {
      Print(
         "SAFETY BLOCK: Account is not Demo."
      );

      return false;
   }

   if(!AllowRealTrading && !IsDemoAccount())
   {
      Print(
         "SAFETY BLOCK: Real trading disabled."
      );

      return false;
   }

   string symbol = FindGoldSymbol();

   if(symbol == "")
   {
      Print("XAUUSD symbol not found.");
      return false;
   }

   if(!SymbolSelect(symbol, true))
   {
      Print(
         "Could not select symbol: ",
         symbol
      );

      return false;
   }

   double lot =
      NormalizeLot(lotText);

   double ask =
      SymbolInfoDouble(
         symbol,
         SYMBOL_ASK
      );

   double bid =
      SymbolInfoDouble(
         symbol,
         SYMBOL_BID
      );

   if(ask <= 0 || bid <= 0)
   {
      Print("Invalid XAUUSD price.");
      return false;
   }

   /*
    * TEST MODE:
    * We use BUY only.
    *
    * This is NOT the final scalping strategy.
    * It is only an execution/connectivity test.
    */

   double sl = 0;
   double tp = 0;

   if(stopLossText != "")
   {
      double slDistance =
         StringToDouble(
            stopLossText
         );

      if(slDistance > 0)
         sl =
            NormalizeDouble(
               ask - slDistance,
               (int)SymbolInfoInteger(
                  symbol,
                  SYMBOL_DIGITS
               )
            );
   }

   if(takeProfitText != "")
   {
      double tpDistance =
         StringToDouble(
            takeProfitText
         );

      if(tpDistance > 0)
         tp =
            NormalizeDouble(
               ask + tpDistance,
               (int)SymbolInfoInteger(
                  symbol,
                  SYMBOL_DIGITS
               )
            );
   }

   trade.SetTypeFillingBySymbol(symbol);

   bool result =
      trade.Buy(
         lot,
         symbol,
         0,
         sl,
         tp,
         "ALJO0D DEMO TEST"
      );

   if(!result)
   {
      Print(
         "BUY failed. Retcode=",
         trade.ResultRetcode(),
         " Description=",
         trade.ResultRetcodeDescription()
      );

      return false;
   }

   Print(
      "DEMO TEST BUY OPENED. Ticket=",
      trade.ResultOrder(),
      " Lot=",
      lot
   );

   return true;
}

//-------------------------------------------------------------------
// PROCESS ROBOT STATE
//-------------------------------------------------------------------
void ProcessRobotState(string json)
{
   bool running =
      JsonBool(
         json,
         "running",
         false
      );

   string lot =
      JsonString(
         json,
         "lot",
         "Minimum"
     
