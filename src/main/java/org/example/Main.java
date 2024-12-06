package org.example;

import org.example.mvc.view.FirstView;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        try(Socket cliSocket = new Socket("172.30.67.203", 8888)) {
            System.out.println("Connection successful");
            FirstView fv = new FirstView();
            fv.firstView();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
