package av.task07.interoperability.DAL.ProjConsts;

public class EndpointConstants {
    public final static String ALL_WORKOUTS_API = "http://localhost:8080/workout/allWorkouts"; // BearerJtw Token needed
    public final static String GET_BY_ID_WORKOUTS_API = "http://localhost:8080/workout/"; // BearerJtw Token needed
    public final static String POST_WORKOUTS_API = "http://localhost:8080/workout/workout"; // BearerJtw Token needed
    public final static String PUT_WORKOUTS_API = "http://localhost:8080/workout/workout/"; // BearerJtw Token needed
    public final static String DELETE_WORKOUTS_API = "http://localhost:8080/workout/workout/"; // BearerJtw Token needed
    public final static String POST_WITH_XSD = "http://localhost:8081/api/workout"; // BearerJtw Token needed
    public final static String POST_WITH_RNG = "http://localhost:8082/apiRNG/workout"; // BearerJtw Token needed
    public final static String SOAP_ENDPOINT = "http://localhost:8083/ws"; // BearerJtw Token needed
    public final static String DHMZ_REST_API = "http://localhost:8085/dhmz/getCities"; // BearerJtw Token not needed
    public final static String DHMZ_RPC = "http://localhost:8085/rpcDhmz/weather/"; // BearerJtw Token not needed /RPC Client
    public final static String AUTH_LOGIN = "http://localhost:8080/auth/login";  //POST Username Password -- returns token
}
