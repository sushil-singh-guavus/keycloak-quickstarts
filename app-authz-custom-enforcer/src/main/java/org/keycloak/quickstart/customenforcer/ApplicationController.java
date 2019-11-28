package org.keycloak.quickstart.customenforcer;


import org.keycloak.AuthorizationContext;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.authorization.AdapterAuthorizationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ApplicationController {

    @RequestMapping(value = "/pipeline", method = RequestMethod.POST,consumes = "application/json")
    public ResponseEntity createPipeline(@RequestBody Pipeline piplineData) {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();

        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getResponse();

        KeycloakSecurityContext keycloakSecurityContext =
                (KeycloakSecurityContext) request
                        .getAttribute(KeycloakSecurityContext.class.getName());

        AuthorizationContext authzContext = keycloakSecurityContext.getAuthorizationContext();
        AdapterAuthorizationContext clientContext = AdapterAuthorizationContext.class.cast(authzContext);
        Map<String, Set<String>> permissionMap = new HashMap();
        Set<String> scopes = new HashSet();
        scopes.add("CREATE");
        String namespace = piplineData.getNamespace();
        String name = piplineData.getName();
        Object resource = new Object();
        permissionMap.put(namespace,scopes);
        clientContext.authorize(permissionMap);

        if (response != null && (response.getHeader("WWW-Authenticate") != null)) {
            return new ResponseEntity(response.getHeader("WWW-Authenticate"),HttpStatus.UNAUTHORIZED);
        }
        else {
            if(response.getStatus()==200 && validatePipelineDetails(name,namespace,resource)) {
                Pipeline pipeline = new Pipeline(name, namespace, resource);
                return new ResponseEntity("Pipeline Created", HttpStatus.CREATED);
            }
        }
        return null;
    }


    public boolean validatePipelineDetails(String name, String namespace, Object resources){
        /* Some Business Logic to create Pipeline
            Saving MetaData Details in different tables
           pipeline name check in current namespace of the user
        */
        return true;
    }
}