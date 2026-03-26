package org.buglaban.travelapi.dto.response;

public class ResponseFailure extends ResponseData {

    public ResponseFailure(int status, String message) {
        super(status, message);
    }
}
