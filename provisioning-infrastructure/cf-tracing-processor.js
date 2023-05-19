const zlib = require('zlib');
const http = require('http');
const { Buffer } = require('buffer');

const {
    OPENTELEMETRY_COLLECTOR_HOSTNAME,
    OPENTELEMETRY_COLLECTOR_OTLP_HTTP_PORT,
    CLOUDFRONT_DOMAIN_NAME
} = process.env;

const keepAliveAgent = new http.Agent({ keepAlive: true, maxSockets: Infinity });
const sendTraceDataOptions = {
    path: '/v1/traces',
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
        'from': 'lambda'
    },
    agent: keepAliveAgent,
    timeout: 2_000,
};

exports.handler = function(event, context) {
    var payload = Buffer.from(event.awslogs.data, 'base64');
    
    zlib.gunzip(payload, function(e, result) {
        if (e) { 
            context.fail(e);
        } else {
            result = JSON.parse(result.toString());

            const resource_spans = result.logEvents.map((logEvent, index) => {
                const { request_id, start_time, end_time, b3 } = logEvent.extractedFields;
                return createTraceDataPayload(request_id, start_time, end_time, b3);
            })

            const payload = {
                resource_spans
            }

            httpRequest(payload, {
                ...sendTraceDataOptions,
                host: OPENTELEMETRY_COLLECTOR_HOSTNAME,
                port: OPENTELEMETRY_COLLECTOR_OTLP_HTTP_PORT
            }).then((data) => {
                console.log('success', data);
                context.succeed();
            }).catch((error) => {
                console.error('fail', error);
                context.fail();
            });
        }
    });
};

function createTraceDataPayload(request_id, start_time, end_time, b3) {
    const _b3 = b3.split('-');
    const traceId = _b3[0];
    const spanId = _b3[1];
    const startTime = parseInt(start_time);
    const endTime = parseInt(end_time);

    return {
        resource: {
            attributes: [
                {
                    "key": "service.name",
                    "value": {
                        "string_value": 'cloudfront'
                    }
                },
                {
                    "key": "telemetry.sdk.language",
                    "value": {
                        "string_value": "nodejs"
                    }
                },
                {
                    "key": "telemetry.sdk.name",
                    "value": {
                        "string_value": "opentelemetry"
                    }
                },
                {
                    "key": "telemetry.sdk.version",
                    "value": {
                        "string_value": "1.0.0"
                    }
                }
            ]
        },
        scope_spans: [
            {
                spans: [
                    {
                        trace_id: traceId,
                        span_id: spanId,
                        start_time_unix_nano: startTime,
                        end_time_unix_nano: endTime,
                        name: CLOUDFRONT_DOMAIN_NAME,
                        kind: 1,
                        attributes: [
                            {
                                "key": "requestId",
                                "value": {
                                    "string_value": request_id
                                }
                            }
                        ]
                    }
                ],
                scope_library: {
                    name: "default"
                }
            }
        ]
    };
}

function httpRequest(payload, options) {
    return new Promise((resolve, reject) => {
        const req = http.request(options, res => {
            let buffer = '';
            res.on('data', chunk => buffer += chunk);
            res.on('end', () => {
                try {
                    resolve(JSON.parse(buffer));
                } catch (err) {
                    reject(new Error(err));
                }
            });
        });
        req.on('error', err => reject(new Error(err)));
        req.on('timeout', () => req.destroy());
        
        req.write(JSON.stringify(payload));
        req.end();
    });
}