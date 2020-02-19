package app;

import mbank.service.LoginInterface;
import mbank.service.Requests;
import util.CommandLineInterface;
import util.Delays;
import util.Http;
import util.Program;

public class App {

    public static void main(String[] args) {
        var credentials = CommandLineInterface.parseCredentials(args);
        var http = new Http("https://online.mbank.pl");
        var requests = new Requests(http);
        var delays = new Delays();
        var loginInterface = new LoginInterface(requests, delays);
        var program = new Program(loginInterface);
        program.run(credentials);
    }

}
