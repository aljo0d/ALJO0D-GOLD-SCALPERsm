import json
import time
import urllib.request
import urllib.error
from datetime import datetime


CONFIG_FILE = "connector_config.json"


def load_config():
    with open(CONFIG_FILE, "r", encoding="utf-8") as file:
        return json.load(file)


def get_status(api_base_url):
    url = api_base_url.rstrip("/") + "/robot/status"

    request = urllib.request.Request(
        url,
        method="GET",
        headers={
            "Accept": "application/json"
        }
    )

    try:
        with urllib.request.urlopen(request, timeout=15) as response:
            data = response.read().decode("utf-8")
            return json.loads(data)

    except urllib.error.HTTPError as error:
        print("HTTP error:", error.code)
        return None

    except Exception as error:
        print("Connection error:", error)
        return None


def print_robot_state(data):
    if not data:
        return

    robot = data.get("robot", {})

    print()
    print("========================================")
    print(" ALJO0D GOLD SCALPER - CONNECTOR")
    print("========================================")
    print("Time:", datetime.now().isoformat())
    print("Running:", robot.get("running"))
    print("Platform:", robot.get("platform"))
    print("Account:", robot.get("accountMode"))
    print("Symbol:", robot.get("symbol"))
    print("Lot:", robot.get("lot"))
    print("Max Trades:", robot.get("maxTrades"))
    print("Stop Loss:", robot.get("stopLoss"))
    print("Take Profit:", robot.get("takeProfit"))
    print("Open Trades:", robot.get("openTrades"))
    print("Floating Profit:", robot.get("floatingProfit"))
    print("Direction:", robot.get("direction"))
    print("========================================")


def main():
    config = load_config()

    api_base_url = config["api_base_url"]
    interval = config.get("poll_interval_seconds", 2)

    print("ALJO0D GOLD SCALPER Connector")
    print("Mode:", config.get("environment"))
    print("Platform:", config.get("platform"))
    print("Symbol:", config.get("symbol"))
    print("Real trading enabled:",
          config.get("real_trading_enabled"))

    if config.get("real_trading_enabled") is not False:
        print("ERROR: Real trading must remain disabled.")
        return

    print()
    print("Connecting to Trading Engine...")
    print("API:", api_base_url)

    while True:

        status = get_status(api_base_url)

        if status and status.get("ok"):

            print_robot_state(status)

        else:

            print(
                datetime.now().isoformat(),
                "- Trading Engine unavailable"
            )

        time.sleep(interval)


if __name__ == "__main__":
    main()
