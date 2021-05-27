/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import equipo0_dominio.TrabajadorSalud;
import equipo0_dominio.Usuario;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import negocios.FactoryNegocios;
import negocios.INegociosAuthTrabSalud;

/**
 * REST Web Service
 *
 * @author Alfonso Felix
 */
@Path("auth")
public class AutenticacionResource {

    @Context
    private UriInfo context;

    private INegociosAuthTrabSalud negocios;
    /**
     * Creates a new instance of AutenticacionResource
     */
    public AutenticacionResource() {
        negocios=FactoryNegocios.getFachadaAuthTrabSalud();
    }

    @POST
    @Path("validartoken")
    @Produces(MediaType.TEXT_PLAIN)
    public Response postValidarToken(String entrada) {
        try {
            return Response.status(200).entity("1").build();
        } catch (Exception ex) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @POST
    @Path("obtenerdatos")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postObtenerDatos(String json) {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            Usuario usuario = gson.fromJson(json, Usuario.class);

            TrabajadorSalud trabSalud = negocios.obtenerDatosTrabSalud(usuario.getUsername());

            if (trabSalud != null) {
                return Response.status(200).entity(gson.toJson(trabSalud)).build();
            }
            return Response.status(404).entity("{0}").build();
        } catch (Exception e) {
            return Response.status(404).entity("{0}").build();
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postLogin(String json) {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            Usuario usuario = gson.fromJson(json, Usuario.class);

            System.out.println(usuario);

            String token = negocios.iniciarSesion(usuario.getUsername(), usuario.getPassword());

            if (token == null) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            } else {
                return String.format("{\"token\":\"%s\"}", token);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
}
