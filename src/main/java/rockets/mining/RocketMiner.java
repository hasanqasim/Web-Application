package rockets.mining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.model.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class RocketMiner {
    private static Logger logger = LoggerFactory.getLogger(RocketMiner.class);

    private DAO dao;

    public RocketMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * TODO: to be implemented & tested!
     * Returns the top-k active rocket, as measured by number of launches.
     *
     * @param k the number of rockets to be returned.
     * @return the list of k most active rockets.
     */
    public List<Rocket> mostLaunchedRockets(int k) {
        logger.info("find most launched " + k + " rockets");
        List<Launch> list = new ArrayList<>(dao.loadAll(Launch.class));
        Map<Rocket, Integer> map = new HashMap<>();
        for (Launch l : list){
            if(map.containsKey(l.getLaunchVehicle()))
            {
                map.put(l.getLaunchVehicle(),map.get(l.getLaunchVehicle())+1);
            }
            else
                map.put(l.getLaunchVehicle(),1);
        }
        return getRockets(map).subList(0,k);
    }

    private List<Rocket> getRockets(Map<Rocket, Integer> map){
        List<Rocket> list = new ArrayList<>(map.keySet());
        for (int i = 0; i <list.size()-1; i++){
            for (int j=i+1; j<list.size(); j++){
                if (map.get(list.get(i)) < map.get(list.get(j))){
                    Rocket rocket = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, rocket);
                }
            }
        }
        return list;
    }

    public List<LaunchServiceProvider> mostReliableLaunchServiceProviders(int k) throws IllegalArgumentException {
        logger.info("find the top-k most reliable launch service providers");
        if (k <= 0)
            throw new IllegalArgumentException("k should be positive");
        List<Launch> list = new ArrayList<>(dao.loadAll(Launch.class));
        Map<LaunchServiceProvider, List<Double>> map = new HashMap<>();
        for (Launch l : list){
            List<Double> arr = new ArrayList<>();
            arr.add(0.00);
            arr.add(0.00);
            if (!map.containsKey(l.getLaunchServiceProvider())){
                if (l.getLaunchOutcome().equals(Launch.LaunchOutcome.SUCCESSFUL)){
                    arr.set(0, 1.00);
                }
                else
                {
                    arr.set(1,1.00);
                }
                map.put(l.getLaunchServiceProvider(), arr);
            }
            else{
                if (l.getLaunchOutcome().equals(Launch.LaunchOutcome.SUCCESSFUL)){
                    double success = arr.get(0) + 1.00;
                    arr.set(0, success);
                }
                else{
                    double fail = arr.get(1) + 1.00;
                    arr.set(1, fail);
                }
                map.replace(l.getLaunchServiceProvider(), arr);
            }
        }
        if (k > map.size())
            k = map.size();
        return sortPercentage(map).subList(0,k);
    }

    private List<LaunchServiceProvider> sortPercentage(Map<LaunchServiceProvider, List<Double>> map){
        List<LaunchServiceProvider> list = new ArrayList<>(map.keySet());
        for (int i = 0; i <list.size()-1; i++){
            for (int j=i+1; j<list.size(); j++){
                List<Double> ilist = map.get(list.get(i));
                double percenti = ilist.get(0)/(ilist.get(0) + ilist.get(1));
                List<Double> jlist = map.get(list.get(j));
                double percentj = jlist.get(0)/(jlist.get(0) + jlist.get(1));
                if (percenti < percentj){
                    LaunchServiceProvider lsp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, lsp);
                }
            }
        }
        return list;
    }

    /**
     * <p>
     * Returns the top-k most recent launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most recent launches.
     */
    public List<Launch> mostRecentLaunches(int k)throws IllegalArgumentException {
        if (k <= 0)
            throw new IllegalArgumentException("k should be positive");
        logger.info("find most recent " + k + " launches");
        List<Launch> launches = new ArrayList<>(dao.loadAll(Launch.class));
        for (int i=0; i<launches.size()-1; i++){
            for(int j=i+1; j<launches.size(); j++){
                if (launches.get(i).getLaunchDate().isBefore(launches.get(j).getLaunchDate())){
                    Launch l = launches.get(i);
                    launches.set(i, launches.get(j));
                    launches.set(j, l);
                }
            }
        }
        if (k> launches.size())
            k = launches.size();
        return launches.subList(0,k);
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the successful launch rate in <code>year</code> measured by the
     * number of successful launches and total number of launches
     *
     * @param year the year
     * @return the successful launch rate in BigDecimal with scale 2.
     */
    public BigDecimal successfulLaunchRateInYear(int year) {
        return BigDecimal.valueOf(0);
    }



    public String dominantCountry(String orbit) throws IllegalArgumentException{
        logger.info("find the dominant country who has the most launched rockets in " + orbit);
        List<Launch> list = new ArrayList<>(dao.loadAll(Launch.class));
        Map<String, Integer> map = new HashMap<>();
        for (Launch l : list){
            if (l.getOrbit().equals(orbit)) {
                if (!map.containsKey(l.getLaunchServiceProvider().getCountry()))
                    map.put(l.getLaunchServiceProvider().getCountry(), 1);
                else {
                    int a = 1 + map.get(l.getLaunchServiceProvider().getCountry());
                    map.replace(l.getLaunchServiceProvider().getCountry(), a);
                }
            }
        }
        if (map.size() == 0)
            throw new IllegalArgumentException("No orbit found");
        return getCountries(map).get(0);
    }

    private List<String> getCountries(Map<String, Integer> map){
        List<String> list = new ArrayList<>(map.keySet());
        for (int i = 0; i <list.size()-1; i++){
            for (int j=i+1; j<list.size(); j++){
                if (map.get(list.get(i)) < map.get(list.get(j))){
                    String country = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, country);
                }
            }
        }
        return list;
    }
    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most expensive launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most expensive launches.
     */
    public List<Launch> mostExpensiveLaunches(int k) throws IllegalArgumentException{
        if (k <= 0)
            throw new IllegalArgumentException("k should be positive");
        logger.info("find most expensive " + k + " launches");
        List<Launch> launches = new ArrayList<>(dao.loadAll(Launch.class));
        for (int i=0; i<launches.size()-1; i++){
            for(int j=i+1; j<launches.size(); j++){
                int flag = launches.get(i).getPrice()<(launches.get(j).getPrice())?-1:0;
                if (flag < 0){
                    Launch l = launches.get(i);
                    launches.set(i, launches.get(j));
                    launches.set(j, l);
                }
            }
        }
        if (k> launches.size())
            k = launches.size();
        return launches.subList(0,k);
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns a list of launch service provider that has the top-k highest
     * sales revenue in a year.
     *
     * @param k the number of launch service provider.
     * @param year the year in request
     * @return the list of k launch service providers who has the highest sales revenue.
     */
    public List<LaunchServiceProvider> highestRevenueLaunchServiceProviders(int k, int year) throws IllegalArgumentException{
        if (k <= 0)
            throw new IllegalArgumentException("k should be positive");
        logger.info("find top" + k + " highest Revenue LaunchServiceProviders");
        List<Launch> list = new ArrayList<>(dao.loadAll(Launch.class));
        Map<LaunchServiceProvider, Integer> map = new HashMap<>();
        for (Launch l : list){
            if (l.getLaunchDate().getYear() == year) {
                if (!map.containsKey(l.getLaunchServiceProvider()))
                    map.put(l.getLaunchServiceProvider(), l.getPrice());
                else {
                    int b = map.get(l);
                    map.replace(l.getLaunchServiceProvider(), l.getPrice()+b);
                }
            }
        }
        if (map.size() == 0)
            throw new IllegalArgumentException("no year found");
        if (k > map.size())
            k = map.size();
        return getList(map).subList(0,k);
    }

    private List getList(Map<LaunchServiceProvider, Integer> map){
        List<LaunchServiceProvider> list = new ArrayList<>(map.keySet());
        for (int i = 0; i <list.size()-1; i++){
            for (int j=i+1; j<list.size(); j++){
                int flag = map.get(list.get(i)).compareTo(map.get(list.get(j)));
                if ( flag < 0 ){
                    LaunchServiceProvider lsp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, lsp);
                }
            }
        }
        return list;
    }

    public List<RocketFamily> haveTheMostRocketsFamily(int k) throws IllegalArgumentException{
        if (k <= 0)
            throw new IllegalArgumentException("k should be positive");
        logger.info("find top" + k + " Rocket Families which contain the most rockets");
        List<RocketFamily> rfs = new ArrayList(dao.loadAll(RocketFamily.class));
        for (int i = 0; i < rfs.size()-1; i++){
            for (int j = i + 1; j < rfs.size(); j++){
                if(rfs.get(i).getRockets().size() < rfs.get(j).getRockets().size()){
                    RocketFamily rf = rfs.get(i);
                    rfs.set(i,rfs.get(j));
                    rfs.set(j,rf);
                }
            }
        }
        if (k > rfs.size())
            k = rfs.size();
        return rfs.subList(0,k);
    }

    public List<Launch> hasTheMostPayloadLaunch(int k) throws IllegalArgumentException{
        if (k <= 0)
            throw new IllegalArgumentException("k should be positive");
        logger.info("find top" + k + " launches which have the most payloads");
        List<Launch> list = new ArrayList<>(dao.loadAll(Launch.class));
        for (int i = 0; i < list.size()-1; i++){
            for (int j = i + 1; j < list.size(); j++){
                if(list.get(i).getPayload().size() < list.get(j).getPayload().size()){
                    Launch l = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j,l);
                }
            }
        }
        if (k > list.size())
            k = list.size();
        return list.subList(0,k);
    }

    public List<Payload> theMostHeavyPayload(int k) throws IllegalArgumentException{
        if (k <= 0)
            throw new IllegalArgumentException("k should be positive");
        logger.info("find top" + k + " most heavy payloads");
        List<Payload> list = new ArrayList(dao.loadAll(Payload.class));
        for (int i = 0; i < list.size()-1; i++){
            for (int j = i + 1; j < list.size(); j++) {
                if(list.get(i).getWeight() < list.get(j).getWeight()) {
                    Payload t = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, t);

                }

            }
        }
        if (k>list.size())
            k = list.size();
        return list.subList(0, k);
    }
}
