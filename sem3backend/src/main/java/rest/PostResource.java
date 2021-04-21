package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PostDTO;
import entities.Favourite;
import errorhandling.API_Exception;
import errorhandling.AlreadyExistsException;
import errorhandling.MissingInputException;
import facades.PostFacade;
import facades.UserFacade;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import facades.DestinationFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;

import utils.EMF_Creator;

@Path("post")
public class PostResource {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final ExecutorService es = Executors.newCachedThreadPool();
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade FACADE = UserFacade.getUserFacade(EMF);
    private static final DestinationFacade DESTINATIONFACADE = DestinationFacade.getDestinationFacade(EMF);
    private static final PostFacade pf = PostFacade.getPostFacade(EMF);
    Gson g = new Gson();

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addPost(String jsonString) throws API_Exception {
        try {
            PostDTO p = g.fromJson(jsonString, PostDTO.class);
            String returnString = pf.newPost(p);
            return g.toJson(returnString);
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied",400,e);
        }
    }

    @GET
    @Path("open/favourites/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getFavorites(@PathParam("user") String user) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        List<Favourite> result = DESTINATIONFACADE.getFavorites(user);
        ArrayList<String> stringResult = new ArrayList<String>();
        
        for (Favourite favourite : result) {
            stringResult.add(favourite.getCountryName());
        }
        
        return stringResult;
    }
    
    @GET
    @Path("open/{country}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getOpenDestination(@PathParam("country") String country) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String result = DestinationFacade.getDestination(country, es, gson);
        return result;
    }
    
    @GET
    @Path("restricted/{country}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRestrictedDestination(@PathParam("country") String country) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String result = DestinationFacade.getDestination(country, es, gson);
        return result;
    }
    
    @POST
    @Path("open/{country}/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String saveFavourite(@PathParam("country") String country, @PathParam("userName") String userName) throws IOException, InterruptedException, ExecutionException, TimeoutException, MissingInputException, AlreadyExistsException {
        String result = gson.toJson(DESTINATIONFACADE.addFavourite(country, userName));
        return result;
    }
    
    @DELETE
    @Path("open/{country}/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteFavourite(@PathParam("country") String country, @PathParam("userName") String userName) throws IOException, InterruptedException, ExecutionException, TimeoutException, MissingInputException, AlreadyExistsException {
        String result = gson.toJson(DESTINATIONFACADE.deleteFavourite(country, userName));
        return result;
    }
    
}
