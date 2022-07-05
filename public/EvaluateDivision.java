package com.example.demo;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class EvaluateDivision {

    public static void main(String[] args) {
        EvaluateDivision ev = new EvaluateDivision();
        List<List<String>> equations = new ArrayList<>();
        equations.add(new ArrayList<String>() { {add("a");add("b");}} );
        equations.add(new ArrayList<String>() { {add("b");add("c");}} );
        equations.add(new ArrayList<String>() { {add("c");add("d");}} );
        equations.add(new ArrayList<String>() { {add("bc");add("cd");}} );


        double[] values = new double[]{1.5, 2.5, 3.0, 5.0};

        List<List<String>> queries = new ArrayList<>();
     /*   queries.add(new ArrayList<String>() { {add("a");add("c");}});
        queries.add(new ArrayList<String>() { {add("b");add("a");}});
        queries.add(new ArrayList<String>() { {add("a");add("e");}});
        queries.add(new ArrayList<String>() { {add("a");add("a");}});
        queries.add(new ArrayList<String>() { {add("x");add("x");}});*/
        queries.add(new ArrayList<String>() { {add("bd");add("ca");}} );
        queries.add(new ArrayList<String>() { {add("bc");add("cd");}});
        queries.add(new ArrayList<String>() { {add("cd");add("bc");}});

        double[] response = ev.calcEquation(equations, values, queries);
        for(double res : response) {
            System.out.println(res);
        }

    }

    public double[] calcEquation(List<List<String>> equations, double[] values,
                                 List<List<String>> queries) {

        HashMap<String, Pair<String, Double>> gidWeight = new HashMap<>();

        // Step 1). build the union groups
        for (int i = 0; i < equations.size(); i++) {
            List<String> equation = equations.get(i);
            String dividend = equation.get(0), divisor = equation.get(1);
            double quotient = values[i];

            union(gidWeight, dividend, divisor, quotient);
            System.out.println(gidWeight);
            //System.exit(1);
            //gidWeight, a , b, 2.0 => (a, (b, 2)) (b, (b, 1))
            //gidWeight, b, c, 3.0 =>  (b, (c, 3)) (c, (c,1))
            // (a, (b, 2)) , (b, (c, 3)), (c, (c, 1))
        }

        // Step 2). run the evaluation, with "lazy" updates in find() function
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            List<String> query = queries.get(i);
            String dividend = query.get(0), divisor = query.get(1);

            if (!gidWeight.containsKey(dividend) || !gidWeight.containsKey(divisor))
                // case 1). at least one variable did not appear before
                results[i] = -1.0;
            else {
                Pair<String, Double> dividendEntry = find(gidWeight, dividend);
                //find(gid, a) => (b, 2)
                Pair<String, Double> divisorEntry = find(gidWeight, divisor);
                //find(gid, c) => (c, 1)
                String dividendGid = dividendEntry.getKey();
                //b
                String divisorGid = divisorEntry.getKey();
                //c
                Double dividendWeight = dividendEntry.getValue();
                //2
                Double divisorWeight = divisorEntry.getValue();
                //1
                if (!dividendGid.equals(divisorGid))
                    // case 2). the variables do not belong to the same chain/group
                    results[i] = -1.0;
                else
                    // case 3). there is a chain/path between the variables
                    results[i] = dividendWeight / divisorWeight;
            }
        }

        return results;
    }

    private Pair<String, Double> find(HashMap<String, Pair<String, Double>> gidWeight, String nodeId) {
        if (!gidWeight.containsKey(nodeId))
            gidWeight.put(nodeId, new Pair<String, Double>(nodeId, 1.0));
        // (a , (a, 1))

        Pair<String, Double> entry = gidWeight.get(nodeId);
        //(b, 2)
        //(c, 3)
        // found inconsistency, trigger chain update
        if (!entry.getKey().equals(nodeId)) {
            Pair<String, Double> newEntry = find(gidWeight, entry.getKey());
            //find(b)
            //(c, 3)

            gidWeight.put(nodeId, new Pair<String, Double>(
                    newEntry.getKey(), entry.getValue() * newEntry.getValue()));
        }

        return gidWeight.get(nodeId);
        //(a, 1)
    }

    private void union(HashMap<String, Pair<String, Double>> gidWeight, String dividend, String divisor, Double value) {
        Pair<String, Double> dividendEntry = find(gidWeight, dividend); // find(gidweight, a)
        // (b, 1)
        Pair<String, Double> divisorEntry = find(gidWeight, divisor); // find(gidweight, b)
        //(c , 1)

        String dividendGid = dividendEntry.getKey();
        //b
        String divisorGid = divisorEntry.getKey();
        //c
        Double dividendWeight = dividendEntry.getValue();
        //1
        Double divisorWeight = divisorEntry.getValue();
        //1

        // merge the two groups together,
        // by attaching the dividend group to the one of divisor
        if (!dividendGid.equals(divisorGid)) {
            gidWeight.put(dividendGid, new Pair<String, Double>(divisorGid,
                    divisorWeight * value / dividendWeight));
            // System.out.println(gidWeight.get("a"));
            //System.exit(1);
            //(a, (b, 1*2/1)) = (a, (b= 2)) OR ( a, {(a, 1), (b, 2)})
            //(b, (c,1*3/1))
        }
    }
}
