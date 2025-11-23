public class budgetChar {
  public void budgetCharacterism() {
    GetTotal g1 = new GetTotal();
    double revenue = g1.getTotalRevenue();
    double expenses = g1.getTotalExpenses(); 
        if (revenue > expenses) {
            System.out.println("Ο προϋπολογισμός είναι πλεονασματικός");
        } else if (revenue < expenses) {
            System.out.println("Ο προϋπολογισμός είναι ελλειματικός");
        } else {
            System.out.println("Ο προϋπολογισμός είναι ισοσκελισμένος");
        }
    }  
}
