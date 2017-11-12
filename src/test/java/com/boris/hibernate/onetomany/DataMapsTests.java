package com.boris.hibernate.onetomany;

import com.boris.model.oneToN.DataMap;
import org.testng.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Щукин on 27.10.2017.
 */
public class DataMapsTests {

    DataService dataService;

    //table A (id, name, caption, b_id(not null))
    //table C (id, xxx, a_id(not_null))

    //table AS (id, a_id, s_id)
    //table S (id, qqq)
    public void test1() {
        DataMap<Long> a = dataService.create("A");
        Assert.assertNotNull(a.getId());

        DataMap<Long> b = dataService.get("B", 7777L);

        a.set("name", "boris")
                .set("caption", "schukin")
                .set("b", b); //N-1

        Assert.assertNotNull(a.get("b"));
        Assert.assertNotNull(a.get("bId"));
        Assert.assertEquals(a.geto("b").getId(), a.get("bId"));


        DataMap<Long> c = new DataMap<>("C", 666L);
        c.set("xxx", "xxx");

        a.<Long>list("slaves").add(c);//1-N

        dataService.flush();
        //insert  into A(id, caption, name, b_id) values ...;
        //insert  into C(id, a_id, xxx values) ...;

        DataMap<Long> s = dataService.create("S");
        a.<Long>list("mmList").add(s);//N-N
        dataService.flush();

        //insert  into S(id, qqq) values ...;
        //insert into AS(id, a_id, qqq_id) values ....
    }

    public void testLoad() {
        DataMap<Long> a = dataService.get("A", 666,
                " {" +
                        "    slaves\n" +
                        "    {" +
                        "      xxx" +
                        "    }\n" +
                        "  }");

        List<DataMap<Long>> aas = dataService.find(
                " A {" +
                        "    bid: :b" +
                        "    slaves\n" +
                        "  }", Collections.singletonMap("b", 777L));


        List<DataMap<Long>> aas2 = dataService.find("A2{}", Collections.emptyMap());

    }

    interface DataService {
        <T> DataMap<T> create(String entity);

        <T> DataMap<T> get(String entity, Object id);

        <T> DataMap<T> get(String entity, Object id, String graphQl);

        <T> List<DataMap<T>> find(String graphQL, Map<String, Object> params);


        void flush();
    }

}
