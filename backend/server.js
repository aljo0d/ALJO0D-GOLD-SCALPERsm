const http = require("http");

const PORT = process.env.PORT || 8080;

let robotState = {
    running: false,
    platform: "MT5",
    accountMode: "Demo",
    symbol: "XAUUSD",
    lot: "Minimum",
    maxTrades: "1 Trade",
    stopLoss: "",
    takeProfit: "",
    floatingProfit: 0,
    openTrades: 0,
    lastTrade: null,
    direction: "WAITING",
    updatedAt: new Date().toISOString()
};

function sendJson(res, statusCode, data) {
    const body = JSON.stringify(data);

    res.writeHead(statusCode, {
        "Content-Type": "application/json; charset=utf-8",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Methods": "GET,POST,OPTIONS",
        "Access-Control-Allow-Headers": "Content-Type"
    });

    res.end(body);
}

function readBody(req) {
    return new Promise((resolve, reject) => {

        let body = "";

        req.on("data", chunk => {
            body += chunk;
        });

        req.on("end", () => {

            if (!body) {
                resolve({});
                return;
            }

            try {
                resolve(JSON.parse(body));
            } catch (error) {
                reject(new Error("Invalid JSON"));
            }
        });

        req.on("error", reject);
    });
}

function now() {
    return new Date().toISOString();
}

const server = http.createServer(async (req, res) => {

    // ============================
    // CORS
    // ============================

    if (req.method === "OPTIONS") {

        res.writeHead(204, {
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET,POST,OPTIONS",
            "Access-Control-Allow-Headers": "Content-Type"
        });

        res.end();
        return;
    }

    try {

        // ============================
        // SAFE PATH DETECTION
        // ============================

        const requestUrl = new URL(
            req.url || "/",
            "http://" + (req.headers.host || "localhost")
        );

        const path = requestUrl.pathname;

        // ============================
        // ROOT / HEALTH
        // ============================

        if (
            req.method === "GET" &&
            (path === "/" || path === "/health")
        ) {

            sendJson(res, 200, {
                ok: true,
                service: "ALJO0D GOLD SCALPER Trading Engine",
                status: "ONLINE",
                time: now()
            });

            return;
        }

        // ============================
        // ROBOT STATUS
        // ============================

        if (
            req.method === "GET" &&
            path === "/robot/status"
        ) {

            sendJson(res, 200, {
                ok: true,
                robot: robotState
            });

            return;
        }

        // ============================
        // ACCOUNT
        // ============================

        if (
            req.method === "GET" &&
            path === "/account"
        ) {

            sendJson(res, 200, {
                ok: true,
                connected: false,
                platform: robotState.platform,
                accountMode: robotState.accountMode,
                symbol: robotState.symbol,
                message:
                    "MT4/MT5 connector is not connected yet."
            });

            return;
        }

        // ============================
        // START ROBOT
        // ============================

        if (
            req.method === "POST" &&
            path === "/robot/start"
        ) {

            const data = await readBody(req);

            robotState.running = true;

            robotState.platform =
                data.platform || "MT5";

            robotState.accountMode =
                data.account_mode || "Demo";

            robotState.symbol =
                data.symbol || "XAUUSD";

            robotState.lot =
                data.lot || "Minimum";

            robotState.maxTrades =
                data.max_trades || "1 Trade";

            robotState.stopLoss =
                data.stop_loss || "";

            robotState.takeProfit =
                data.take_profit || "";

            robotState.updatedAt = now();

            sendJson(res, 200, {
                ok: true,
                action: "START",
                message: "Robot start command accepted.",
                robot: robotState
            });

            return;
        }

        // ============================
        // STOP ROBOT
        // ============================

        if (
            req.method === "POST" &&
            path === "/robot/stop"
        ) {

            robotState.running = false;
            robotState.direction = "WAITING";
            robotState.updatedAt = now();

            sendJson(res, 200, {
                ok: true,
                action: "STOP",
                message: "Robot stop command accepted.",
                robot: robotState
            });

            return;
        }

        // ============================
        // DEBUG ROUTE
        // ============================

        if (
            req.method === "GET" &&
            path === "/debug"
        ) {

            sendJson(res, 200, {
                ok: true,
                method: req.method,
                url: req.url,
                path: path,
                host: req.headers.host,
                time: now()
            });

            return;
        }

        // ============================
        // NOT FOUND
        // ============================

        sendJson(res, 404, {
            ok: false,
            error: "Endpoint not found",
            method: req.method,
            path: path,
            url: req.url
        });

    } catch (error) {

        sendJson(res, 500, {
            ok: false,
            error: error.message
        });
    }
});

server.listen(PORT, "0.0.0.0", () => {

    console.log(
        "ALJO0D GOLD SCALPER Trading Engine running on port " +
        PORT
    );

});
