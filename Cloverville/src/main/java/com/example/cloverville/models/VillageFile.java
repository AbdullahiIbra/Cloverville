package com.example.cloverville.models;

import java.io.*;
import java.util.ArrayList;

public class VillageFile {

  private static final String FILE_NAME = "village.json";

  // -------------------------------------------------------------
  // SAVE VILLAGE TO JSON
  // -------------------------------------------------------------
  public static void save(Village v) {
    try (FileWriter fw = new FileWriter(FILE_NAME)) {

      fw.write("{\n");

      // Save residents
      fw.write("\"residents\": [\n");
      ArrayList<Resident> rList = v.getResidents().getAll();

      for (int i = 0; i < rList.size(); i++) {
        Resident r = rList.get(i);
        fw.write("  {");
        fw.write("\"name\": \"" + r.getName() + "\", ");
        fw.write("\"points\": " + r.getPoints() + ", ");
        fw.write("\"gender\": \"" + r.getGender() + "\", ");
        fw.write("\"day\": " + r.getBirthDay().getDay() + ", ");
        fw.write("\"month\": " + r.getBirthDay().getMonth() + ", ");
        fw.write("\"year\": " + r.getBirthDay().getYear());
        fw.write("}");
        if (i < rList.size() - 1) fw.write(",");
        fw.write("\n");
      }
      fw.write("],\n");

      // Save communal tasks
      fw.write("\"tasks\": [\n");
      ArrayList<CommunalTask> tList = v.getTasks().getAll();

      for (int i = 0; i < tList.size(); i++) {
        CommunalTask t = tList.get(i);
        fw.write("  {");
        fw.write("\"name\": \"" + t.getTaskName() + "\", ");
        fw.write("\"points\": " + t.getTaskPoints() + ", ");
        fw.write("\"description\": \"" + t.getDescription() + "\"");
        fw.write("}");
        if (i < tList.size() - 1) fw.write(",");
        fw.write("\n");
      }
      fw.write("],\n");

      // Save green actions
      fw.write("\"greenActions\": [\n");
      ArrayList<GreenAction> gList = v.getGreenActions().getAll();

      for (int i = 0; i < gList.size(); i++) {
        GreenAction g = gList.get(i);
        fw.write("  {");
        fw.write("\"name\": \"" + g.getTaskName() + "\", ");
        fw.write("\"points\": " + g.getPoints() + ", ");
        fw.write("\"description\": \"" + g.getDescription() + "\"");
        fw.write("}");
        if (i < gList.size() - 1) fw.write(",");
        fw.write("\n");
      }
      fw.write("],\n");

      // Save trade offers
      fw.write("\"tradeOffers\": [\n");
      ArrayList<TradeOffer> oList = v.getOffers().getAll();

      for (int i = 0; i < oList.size(); i++) {
        TradeOffer o = oList.get(i);
        fw.write("  {");
        fw.write("\"name\": \"" + o.getTaskName() + "\", ");
        fw.write("\"points\": " + o.getTaskPoints() + ", ");
        fw.write("\"description\": \"" + o.getDescription() + "\", ");
        fw.write("\"seller\": \"" + o.getSeller().getName() + "\"");
        fw.write("}");
        if (i < oList.size() - 1) fw.write(",");
        fw.write("\n");
      }
      fw.write("],\n");

      // Save community points
      fw.write("\"communityPoints\": " + v.getGreenActions().getCommunityPoints() + "\n");

      fw.write("}");

      System.out.println("✔ Village saved to " + FILE_NAME);

    } catch (Exception e) {
      System.out.println("❌ Error saving file.");
      e.printStackTrace();
    }
  }

  // -------------------------------------------------------------
  // LOAD VILLAGE FROM JSON
  // -------------------------------------------------------------
  public static Village load() {
    Village v = new Village("CloverVille");

    File file = new File(FILE_NAME);
    if (!file.exists()) {
      System.out.println("⚠ No save file found. A new village will be created.");
      return v;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

      StringBuilder json = new StringBuilder();
      String line;

      while ((line = br.readLine()) != null)
        json.append(line.trim());

      String text = json.toString();

      // --------------------------
      // Load residents
      // --------------------------
      String[] residentBlocks = text.split("\"residents\":")[1]
          .split("]")[0]
          .replace("[", "")
          .split("\\},\\{");

      for (String block : residentBlocks) {

        block = block.replace("{", "").replace("}", "");

        if (block.trim().isEmpty()) continue;

        String name = getValue(block, "name");
        int points = Integer.parseInt(getValue(block, "points"));
        char gender = getValue(block, "gender").charAt(0);
        int d = Integer.parseInt(getValue(block, "day"));
        int m = Integer.parseInt(getValue(block, "month"));
        int y = Integer.parseInt(getValue(block, "year"));

        v.getResidents().add(
            new Resident(name, points, gender, new MyDate(d, m, y))
        );
      }

      // --------------------------
      // Load communal tasks
      // --------------------------
      String[] taskBlocks = text.split("\"tasks\":")[1]
          .split("]")[0]
          .replace("[", "")
          .split("\\},\\{");

      for (String block : taskBlocks) {
        block = block.replace("{", "").replace("}", "");
        if (block.trim().isEmpty()) continue;

        String name = getValue(block, "name");
        int points = Integer.parseInt(getValue(block, "points"));
        String desc = getValue(block, "description");

        v.getTasks().addTask(
            new CommunalTask(name, points, desc, null)
        );
      }

      // --------------------------
      // Load green actions
      // --------------------------
      String[] greenBlocks = text.split("\"greenActions\":")[1]
          .split("]")[0]
          .replace("[", "")
          .split("\\},\\{");

      for (String block : greenBlocks) {
        block = block.replace("{", "").replace("}", "");
        if (block.trim().isEmpty()) continue;

        String name = getValue(block, "name");
        int points = Integer.parseInt(getValue(block, "points"));
        String desc = getValue(block, "description");

        v.getGreenActions().addGreenAction(
            new GreenAction(name, points, desc)
        );
      }

      // --------------------------
      // Load trade offers
      // --------------------------
      String[] offerBlocks = text.split("\"tradeOffers\":")[1]
          .split("]")[0]
          .replace("[", "")
          .split("\\},\\{");

      for (String block : offerBlocks) {
        block = block.replace("{", "").replace("}", "");
        if (block.trim().isEmpty()) continue;

        String name = getValue(block, "name");
        int points = Integer.parseInt(getValue(block, "points"));
        String desc = getValue(block, "description");
        String sellerName = getValue(block, "seller");

        Resident seller = findResident(v, sellerName);

        v.getOffers().addOffer(
            new TradeOffer(name, points, desc, null, seller)
        );
      }

      // --------------------------
      // Load community points
      // --------------------------
      String cp = text.split("\"communityPoints\":")[1]
          .replace("}", "")
          .trim();
      v.getGreenActions().setCommunityPoints(Integer.parseInt(cp));

      System.out.println("✔ Village loaded successfully.");

    } catch (Exception e) {
      System.out.println("❌ Error loading file.");
      e.printStackTrace();
    }

    return v;
  }

  // -------------------------------------------------------------
  // Utility methods
  // -------------------------------------------------------------

  private static String getValue(String block, String key) {
    try {
      return block.split("\"" + key + "\":")[1]
          .split(",")[0]
          .replace("\"", "")
          .trim();
    } catch (Exception e) {
      return "";
    }
  }

  private static Resident findResident(Village v, String name) {
    for (Resident r : v.getResidents().getAll()) {
      if (r.getName().equals(name)) return r;
    }
    return null;
  }
}
