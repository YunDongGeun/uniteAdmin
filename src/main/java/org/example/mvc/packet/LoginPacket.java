package org.example.mvc.packet;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.global.Protocol;

import java.util.Objects;

@Getter
@Setter
public class LoginPacket extends Protocol {

    public LoginPacket(String data, String code) {
        if(Objects.equals(code, "id")) {
            this.type = Protocol.TYPE_AUTH;
            this.code = Protocol.CODE_AUTH_ID_REQ;
            this.length = (short) data.length();
            setData(data.getBytes());
        } else if (Objects.equals(code, "pwd")) {
            super.type = Protocol.TYPE_AUTH;
            super.code = Protocol.CODE_AUTH_PW_REQ;
            super.length = (short) data.length();
            setData(data.getBytes());
        }
    }

}

