package net.parchat.parcord.paper.handlers;

import javax.security.auth.login.LoginException;

public interface ModuleHandler {

    void enable() throws LoginException, InterruptedException;

    void disable();

}