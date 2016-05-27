package com.livechat.adapter;

public class ChatApiException extends Exception {
        public String message;
        public String reason;
        public int rc;
        public int httpStatusCode;
        public String time;

        public ChatApiException() {
                this(-1);
        }

        public String getMessage() {
                return message;
        }

        public String getReason() {
                return reason;
        }

        public int getRc() {
                return rc;
        }

        public int getHttpStatusCode() {
                return httpStatusCode;
        }

        public String getTime() {
                return time;
        }

        public ChatApiException(int httpCode) {
                message = "NA";
                reason = "NA";
                this.httpStatusCode = httpCode;
        }

        public String toString() {
                return "An error was received from the LivePerson REST API. " + time + " rc=" + rc + " message=" + message + " reason=" + reason + " HTTP Error Code=" + httpStatusCode;
        }
}
