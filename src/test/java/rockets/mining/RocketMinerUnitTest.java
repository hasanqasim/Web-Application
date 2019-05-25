package rockets.mining;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.dataaccess.neo4j.Neo4jDAO;
import rockets.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RocketMinerUnitTest {
    Logger logger = LoggerFactory.getLogger(RocketMinerUnitTest.class);

    private DAO dao;
    private RocketMiner miner;
    private List<Rocket> rockets;
    private List<LaunchServiceProvider> lsps;
    private List<Launch> launches;
    private Set<Rocket> rset;
    private List<RocketFamily> rfs;
    private List<Payload> pList;

    @BeforeEach
    public void setUp() {
        dao = mock(Neo4jDAO.class);
        miner = new RocketMiner(dao);
        rockets = Lists.newArrayList();
        rset = new HashSet();
        lsps = Lists.newArrayList(
                new LaunchServiceProvider("ULA", 1990, "USA"),
                new LaunchServiceProvider("SpaceX", 2002, "USA"),
                new LaunchServiceProvider("ESA", 1975, "Europe ")
        );
        List<Integer> prices = Arrays.asList(
                1000000,
                2222222,
                7777777,
                2333333,
                5555555
        );
        // index of lsp of each rocket
        int[] lspIndex    = new int[]{0, 0, 0, 1, 1};

        // 5 rockets
        for (int i = 0; i < 5; i++) {
            rockets.add(new Rocket("rocket_" + i, "USA", lsps.get(lspIndex[i])));
            rset.add(new Rocket("rocket_" + i, "USA", lsps.get(lspIndex[i])));
        }
        when(dao.loadAll(Rocket.class)).thenReturn(rockets);
        Set<Rocket> set1 = new HashSet<>();
        set1.add(spy(rockets.get(0)));
        set1.add(spy(rockets.get(1)));
        Set<Rocket> set2 = new HashSet<>();
        set2.add(spy(rockets.get(1)));
        rfs = Arrays.asList(
                spy(new RocketFamily("Astar1", rset)),
                spy(new RocketFamily("Astar2", set1)),
                spy(new RocketFamily("Astar3", set2))
        );
        // month of each launch
        int[] months = new int[]{1, 6, 4, 3, 4, 11, 6, 5, 12, 5};

        // index of rocket of each launch
        int[] rocketIndex = new int[]{0, 0, 0, 0, 1, 1, 1, 2, 2, 3};
        String[] orbits = new String[]{"LEO", "GTO"};
        int[] lspsIndex = new int[]{0, 0, 0, 0, 0, 1, 1, 1, 1, 2};
        int[] orbitIndex = new int[]{0,0,0,0,0,0,1,1,1,1};
        int[] priceIndex = new int[]{0,1,2,3,4,0,1,2,3,4};
        Launch.LaunchOutcome[] lo = new Launch.LaunchOutcome[]{Launch.LaunchOutcome.SUCCESSFUL, Launch.LaunchOutcome.FAILED};

        pList = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            pList.add(new Payload("payload_"+i, "Category_" + i, 1000*i, true));
        Set<Payload> pset1 = new HashSet<>();
        pset1.add(spy(pList.get(0)));
        pset1.add(spy(pList.get(1)));
        pset1.add(spy(pList.get(2)));
        Set<Payload> pset2 = new HashSet<>();
        pset1.add(spy(pList.get(0)));
        pset1.add(spy(pList.get(1)));
        List<Set> payLoads = new ArrayList<>();
        payLoads.add(set1);
        payLoads.add(set2);

        // 10 launches
        launches = IntStream.range(0, 10).mapToObj(i -> {
            logger.info("create " + i + " launch in month: " + months[i]);
            Launch l = new Launch();
            l.setLaunchDate(LocalDate.of(2017, months[i], 1));
            l.setLaunchVehicle(rockets.get(rocketIndex[i]));
            l.setPrice(prices.get(priceIndex[i]));
            l.setLaunchServiceProvider(lsps.get(lspsIndex[i]));
            l.setLaunchSite("VAFB");
            l.setOrbit(orbits[orbitIndex[i]]);
            l.setLaunchOutcome(lo[orbitIndex[i]]);
            l.setPayloads(payLoads.get(orbitIndex[i]));
            spy(l);
            return l;
        }).collect(Collectors.toList());

        rockets.get(0).setLaunches(Sets.newHashSet(launches.subList(0, 4)));
        rockets.get(1).setLaunches(Sets.newHashSet(launches.subList(4, 7)));
        rockets.get(2).setLaunches(Sets.newHashSet(launches.subList(7, 9)));
        rockets.get(3).setLaunches(Sets.newHashSet(launches.get(9)));
    }




    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnMostReliableLaunchServiceProviders(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<LaunchServiceProvider> Actuallist = miner.mostReliableLaunchServiceProviders(k);
        Map<LaunchServiceProvider, List<Double>> map = new HashMap<>();
        for (Launch l : launches){
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
        assertEquals(k, Actuallist.size());
        assertEquals(list.subList(0, k), Actuallist);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void shouldThrowExceptionWhileKIsNegativeInMostReliable(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.mostReliableLaunchServiceProviders(k));
        assertEquals("k should be positive", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostLaunchedRockets(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Rocket> sortedRockets = new ArrayList<>(rockets);
        List<Rocket> loadedRockets = miner.mostLaunchedRockets(k);
        assertEquals(k, loadedRockets.size());
        assertEquals(sortedRockets.subList(0,k), loadedRockets.subList(0,k));

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostRecentLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        for (int i=0; i<launches.size()-1; i++){
            for(int j=i+1; j<launches.size(); j++){
                if (launches.get(i).getLaunchDate().isBefore(launches.get(j).getLaunchDate())){
                    Launch l = launches.get(i);
                    launches.set(i, launches.get(j));
                    launches.set(j, l);
                }
            }
        }
        List<Launch> loadedLaunches = miner.mostRecentLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(launches.subList(0, k), loadedLaunches);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void shouldThrowExceptionWhileKIsNegativeInMostRecent(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.mostRecentLaunches(k));
        assertEquals("k should be positive", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostExpensiveLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> b.getPrice()-a.getPrice());
        List<Launch> loadedLaunches = miner.mostExpensiveLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void shouldThrowExceptionWhileKIsNegativeInMostExpensive(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.mostExpensiveLaunches(k));
        assertEquals("k should be positive", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaa", "bbb"})
    public void shouldThrowExceptionWhileOrbitIsNotFoundInDominant(String orbit) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.dominantCountry(orbit));
        assertEquals("No orbit found", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"LEO", "GTO"})
    public void shouldReturndominantCountry(String orbit) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> list = new ArrayList<>(launches);
        String country = miner.dominantCountry(orbit);
        Map<String, Integer> map = new HashMap<>();
        for (Launch l : list) {
            if (l.getOrbit().equals(orbit)) {
                if (!map.containsKey(l.getLaunchServiceProvider().getCountry()))
                    map.put(l.getLaunchServiceProvider().getCountry(), 1);
                else {
                    int a = 1 + map.get(l.getLaunchServiceProvider().getCountry());
                    map.replace(l.getLaunchServiceProvider().getCountry(), a);
                }
            }
        }
        int[] arr = new int[map.size()];
        List<Integer> l = new ArrayList(map.values()) {
        };
        for (int i = 0; i < l.size(); i++) {
            arr[i] = l.get(i);
        }
        int max = arr[0];
        for (int i : arr) {
            if (max < i)
                max = i;
        }
        Set<String> set = map.keySet();
        String expected = "";
        for (String s : set) {
            if (map.get(s) == max)
                expected = s;
        }
        assertEquals(expected, country);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostRocketFamilies(int k) {
        when(dao.loadAll(RocketFamily.class)).thenReturn(rfs);
        List<RocketFamily> list = new ArrayList<>(dao.loadAll(RocketFamily.class));
        List<RocketFamily> loadedrfs = miner.haveTheMostRocketsFamily(k);
        assertEquals(k, loadedrfs.size());
        assertEquals(list.subList(0,k), loadedrfs.subList(0,k));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void shouldThrowExceptionWhileKIsNegativeInTopMostFamily(int k) {
        when(dao.loadAll(RocketFamily.class)).thenReturn(rfs);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.haveTheMostRocketsFamily(k));
        assertEquals("k should be positive", exception.getMessage());
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnLaunchesWithMostPayloads(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> loaded = miner.hasTheMostPayloadLaunch(k);
        assertEquals(k, loaded.size());
        for(int i=0; i<loaded.size()-1;i++){
            for(int j=i+1; j<loaded.size();j++){
                Assertions.assertTrue(loaded.get(i).getPayload().size() >= loaded.get(j).getPayload().size());
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void shouldThrowExceptionWhileKIsNegativeInLaunchesWithMostPayload(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.hasTheMostPayloadLaunch(k));
        assertEquals("k should be positive", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnMostHeavyPayloads(int k) {
        when(dao.loadAll(Payload.class)).thenReturn(pList);
        List<Payload> loaded = miner.theMostHeavyPayload(k);
        assertEquals(k, loaded.size());
        for(int i=0; i<loaded.size()-1;i++){
            for(int j=i+1; j<loaded.size();j++){
                Assertions.assertTrue(loaded.get(i).getWeight()>=loaded.get(j).getWeight());
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void shouldThrowExceptionWhileKIsNegativeInMostHeavyPayload(int k) {
        when(dao.loadAll(Payload.class)).thenReturn(pList);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> miner.theMostHeavyPayload(k));
        assertEquals("k should be positive", exception.getMessage());
    }

}