var crypto = require('crypto');

function handler(event) {
    var request = event.request;
    var context = event.context;
    var headers = request.headers;
    var startTimeUnixNano = Date.now() * 1000000;
    var hex = convertBase64ToHex(context.requestId)
    var traceId = hex.substring(0, 32);
    var spanId = hex.substring(32, 48)
    var b3SampledFlag = 1;
    var sampleRate = 1;

    headers['b3'] = {
        value: `${traceId}-${spanId}-${b3SampledFlag}`
    };

    headers['trace-start-time'] = {
        value: startTimeUnixNano.toString()
    };

    headers['trace-sample-rate'] = {
        value: sampleRate.toString()
    };

    return request;
}

function convertBase64ToHex(base64EncodedText) {
    return crypto.createHash("sha256").update(base64EncodedText).digest('hex');
}