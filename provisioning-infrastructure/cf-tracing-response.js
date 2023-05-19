function handler(event) {
    var response = event.response;
    var headers = response.headers;
    var endTimeUnixNano = Date.now() * 1000000;
    var traceSampleRate = parseFloat(event.request.headers['trace-sample-rate'].value);

    if (Math.random() < traceSampleRate) {
        console.log(event.request.headers['trace-start-time'].value + ' ' + endTimeUnixNano.toString() + ' ' + event.request.headers['b3'].value);
        headers['b3'] = {value: event.request.headers['b3'].value}
    }

    return response;
}