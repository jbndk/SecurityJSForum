package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PostDTO;
import entities.Post;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.concurrent.*;


public class PostFacade {

    private static EntityManagerFactory emf;
    private static PostFacade instance;

    private PostFacade() {

    }

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static PostFacade getPostFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PostFacade();
        }
        return instance;
    }

    public static String newPost(PostDTO p) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        EntityManager em = emf.createEntityManager();

        Post post = new Post(p.getCategory(), p.getImageurl(), p.getContent(), p.getAuthor());

        try {

            em.getTransaction().begin();
            em.persist(post);
            em.getTransaction().commit();
            return "Post successfully added";
        } catch (Exception e){
            throw e;
        } finally {
            em.close();
        }
    }


/*
    public static CovidInfoDTO getCovidInfo(String countryAlpha, ExecutorService threadPool, final Gson gson) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        Callable<CovidInfoDTO> destTask = new Callable<CovidInfoDTO>() {
            @Override
            public CovidInfoDTO call() throws IOException {

                String rates = HttpUtils.fetchData(COVID_SERVER + countryAlpha);

                CovidInfoDTO covidDTO = gson.fromJson(rates, CovidInfoDTO.class);

                return covidDTO;
            }
        };

        Future<CovidInfoDTO> future = threadPool.submit(destTask);

        CovidInfoDTO result = future.get();

        return result;
    }

    public static RateDTO getRate(String code) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        String rates = HttpUtils.fetchData(RATES_SERVER + code);

        ObjectMapper objectMapper = new ObjectMapper();

        RateDTO ratedto = new RateDTO();

        try {
            JsonNode node = objectMapper.readValue(rates, JsonNode.class);
            JsonNode child = node.get("rates");
            JsonNode childField = child.get(code);
            Double rate = childField.asDouble();
            System.out.println(rate);
            ratedto.setRate(rate);
            System.out.println(ratedto);

        } catch (IOException e) {
        }
        return ratedto;

    }

    public List<Post> getFavorites(String user) {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Post> query = em.createQuery("select f FROM Post f INNER JOIN f.users User WHERE User.userName = :user", Post.class);
        query.setParameter("user", user);
        List<Post> resultList = query.getResultList();
        return resultList;
    }

    public String deleteFavourite(String country, String userName) throws MissingInputException {

        EntityManager em = emf.createEntityManager();

        try {

            TypedQuery<Post> query = em.createQuery("SELECT f FROM Post f WHERE f.countryName = :country", Post.class);
            query.setParameter("country", country);
            Post post = query.getSingleResult();

            User user = em.find(User.class, userName);

            em.getTransaction().begin();
            user.removeFavourite(post);
            em.getTransaction().commit();

        } finally {
            em.close();
        }

        return country;
    }

    public String addFavourite(String country, String userName) throws MissingInputException {

        String lowerCountry = country.toLowerCase();

        EntityManager em = emf.createEntityManager();

        if (lowerCountry.length() == 0 || (userName.length() == 0)) {
            throw new MissingInputException("One or both values are missing");
        }

        String returnString = "";

        Post post = new Post(lowerCountry);

        //Checks if favourite already exists in DB:
        try {

            TypedQuery<Post> query = em.createQuery("SELECT f FROM Post f WHERE f.countryName = :country", Post.class);
            query.setParameter("country", lowerCountry);
            Post result = query.getSingleResult();

            post = result;

        } catch (Exception e) {
            System.out.println("Failed to find favourite in DB");
        }

        List<Post> usersFavorites = getFavorites(userName);

        //Sikrer at vi kun sammenligner CountryName og ikke ID'et/key'en:
        boolean favoriteExists = usersFavorites.stream().anyMatch(o -> o.getCountryName().equals(lowerCountry));

        if (favoriteExists == true) {
            System.out.println(getFavorites(userName));
            returnString = "You have already saved the destination";
            return returnString;

        } else {

            try {
//
                User user = em.find(User.class, userName);

                user.addFavourite(post);

                em.getTransaction().begin();
                em.persist(user);
                em.getTransaction().commit();

                returnString = post.getCountryName();

                return returnString;
            } finally {
                em.close();
            }
        }

    }
*/
}
