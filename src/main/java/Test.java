import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Until;
import layers.layers.Dense;
import layers.layers.DenseConfig;
import layers.model.Model;

import java.util.HashMap;

class A {

}

class B extends A {
    int a;
}

class Config {
    @Until(0.1)
    int b;
    int t;
}

class Config2 extends Config {
    int c;
    Object[][][] objects;
}

class Layer {
    Config config;
}

public class Test {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().serializeNulls().setVersion(1.0).create();
        Layer t = new Layer();
        t.config = new Config2();
        ((Config2) t.config).objects = new Object[1][][];
        ((Config2) t.config).objects[0] = new Object[1][];
        ((Config2) t.config).objects[0][0] = new Object[4];
        ((Config2) t.config).objects[0][0][0] = "add";
        ((Config2) t.config).objects[0][0][1] = 0;
        ((Config2) t.config).objects[0][0][2] = 0;
        ((Config2) t.config).objects[0][0][3] = new HashMap<>();
        System.out.print(gson.toJson(t));

        Dense d = new Dense();
        System.out.println(d.getConfigureList());
        System.out.println(gson.toJson(d));
        try {
            d.setString("name", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(gson.toJson(d));


        System.out.println(d.isStringConfig("name"));
        String[] lst = d.getConfigurableObject("config").getSelection("dtype");
        try {
            d.getConfigurableObject("config").setString("name", "abc");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (String str : d.getConfigurableObject("config").getConfigureList()) {
            System.out.println(str);
        }
        System.out.println(gson.toJson(d));


        DenseConfig denseConfig = (DenseConfig) d.getConfigurableObject("config");
        for (String str : denseConfig.getSelection("kernel_initializer")) {
            System.out.println(str);
        }

        System.out.println("--------");

        Dense d1 = new Dense();
        d1.init();
        Model model = new Model();
        model.config.addLayer(d1);
        System.out.println(gson.toJson(model));
        model.config.deleteLayer(d1);
        System.out.println(gson.toJson(model));
        Dense d2 = new Dense();
        d2.init();
        model.config.addLayer(d1);
        model.config.addLayer(d2);
        System.out.println("--------");
        System.out.println(gson.toJson(model));
        System.out.println("--------");
        model.config.addEdge(d1, d2);
        System.out.println(gson.toJson(model));
        System.out.println("--------");
        model.config.deleteEdge(d1, d2);
        System.out.println(gson.toJson(model));


    }
}
