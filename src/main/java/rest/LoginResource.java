package rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dtos.LoginDTO;
import facades.UserFacade;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import entities.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import security.SharedSecret;
import security.errorhandling.AuthenticationException;
import errorhandling.GenericExceptionMapper;

@Path("login")
public class LoginResource extends Resource {
    public static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30 min
    public static final UserFacade USER_FACADE = UserFacade.getFacade(EMF);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String loginJson) throws AuthenticationException {
        LoginDTO loginDTO = GSON.fromJson(loginJson, LoginDTO.class);

        try {
            User user = USER_FACADE.getVerifiedUser(loginDTO.getUsername(), loginDTO.getPassword());
            String token = createToken(user.getId(), user.getUsername(), user.getRolesAsStringList());

            loginDTO = new LoginDTO.Builder()
                    .setUsername(user.getUsername())
                    .setToken(token)
                    .build();

            return Response.ok(GSON.toJson(loginDTO)).build();
        } catch (JOSEException | AuthenticationException ex) {
            if (ex instanceof AuthenticationException) {
                throw (AuthenticationException) ex;
            }
            Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new AuthenticationException("Invalid username or password! Please try again");
    }

    private String createToken(int userId, String userName, List<String> roles) throws JOSEException {
        String rolesAsString = getListAsString(roles);
        String issuer = "ThomasStudios";

        JWSSigner signer = new MACSigner(SharedSecret.getSharedKey());
        Date date = new Date();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(userId))
                .claim("name", userName)
                .claim("roles", rolesAsString)
                .claim("issuer", issuer)
                .issueTime(date)
                .expirationTime(new Date(date.getTime() + TOKEN_EXPIRE_TIME))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    private String getListAsString(List<String> stringList) {
        StringBuilder res = new StringBuilder();
        for (String string : stringList) {
            res.append(string);
            res.append(",");
        }
        return res.length() > 0 ? res.substring(0, res.length() - 1) : "";
    }
}
