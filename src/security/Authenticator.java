/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import dbhandler.DBConnector;
import dbhandler.Hasher;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.AuthenticationException;
import org.owasp.esapi.errors.EncryptionException;

/**
 * This class manages authentication operations used by the application
 * @author miguelgomez
 */
public class Authenticator implements org.owasp.esapi.Authenticator{

    @Override
    public void clearCurrent() {
        //Delete any existing session and session objects
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User login() throws AuthenticationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User login(HttpServletRequest hsr, HttpServletResponse hsr1) throws AuthenticationException {
        int userID = -1;
        String userName = null;
        String userPassword = null;
        dbhandler.User user = null;
        DBConnector dbConnector = null;
        try {
            //Called by Authentication servlet
            
            //Instantiate DB connection
            //Secure this later
            dbConnector = new DBConnector("localhost", 3306, "root", "xgbawesome", "foobar");
            //Extract from request
            userName = hsr.getParameter("user_name");
            userPassword = hsr.getParameter("user_password");
            //Validate user input
            //Convert password to MD5 hash
            
            //Once validated for form, authenticate
            userID = dbConnector.authenticateUser(userName, userPassword);
            if(userID != -1){
            //Get user details
                user = dbConnector.getUserDetails(userID);
                user.setLoggedIn(true);
            }else{
                throw new AuthenticationException("Please register a new account", 
                                                  "Non-existant user attempted to login");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Authenticator.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            throw new AuthenticationException("Login temporarily unavailable", "SQL Server Error");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Authenticator.class.getName()).log(Level.SEVERE, null, ex);
            throw new AuthenticationException("Login temporarily unavailable", "SQL Server Driver Error");
        } finally{
            return user;
        }
    }

    public static void assertCurrentUser(HttpSession userSession,
                                         String clientAddress) throws AuthenticationException, 
                                                                      NoSuchAlgorithmException, 
                                                                      UnsupportedEncodingException{
        String challenge = "";
        User loggedInUser = null;
        String storedSalt = "";
        String clientToken = "";
        Hasher hashGenerator = null;
        //Check if user is already logged in
        loggedInUser = (User)userSession.getAttribute("session_user");
        //If not logged in, redirect to the login page. 
    
        if(loggedInUser == null || 
            !loggedInUser.isLoggedIn()){
            throw new AuthenticationException("Please log in to use the system", 
                                              "Unauthenticated user attempted access");
        }else{
            //Get session salt
            storedSalt = (String)userSession.getAttribute("session_salt");
            //Initialize hash generator
            hashGenerator = new Hasher(Hasher.SHA256);
            hashGenerator.updateHash(clientAddress, "UTF-8");
            hashGenerator.updateHash(storedSalt, "UTF-8");
            challenge = hashGenerator.getHashBASE64();
            //Get client token
            clientToken = (String)userSession.getAttribute("session_token");
            //Check if the token does not exist or does not match
            if(clientToken == null || !clientToken.equals(challenge)){
                throw new AuthenticationException("Please log in to use the system", 
                                                  "An invalid session token is present");
            }
        }
    }
    
    @Override
    public boolean verifyPassword(User user, String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void logout() {
        //Same as the clear current
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User createUser(String string, String string1, String string2) throws AuthenticationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateStrongPassword() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateStrongPassword(User user, String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void changePassword(User user, String string, String string1, String string2) throws AuthenticationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User getUser(long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User getUser(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set getUserNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User getCurrentUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCurrentUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String hashPassword(String string, String string1) throws EncryptionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeUser(String string) throws AuthenticationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void verifyAccountNameStrength(String string) throws AuthenticationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void verifyPasswordStrength(String string, String string1, User user) throws AuthenticationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean exists(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
