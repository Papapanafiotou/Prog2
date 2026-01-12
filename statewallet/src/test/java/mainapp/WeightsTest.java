@Test
void testGetWeightValidAndInvalid() {
    // abc -> σφάλμα, 2.0 -> εκτός ορίων, 0.5 -> σωστό
    // Προσθέτουμε πολλές αλλαγές γραμμής στο τέλος για να μην "στεγνώσει" ο Scanner
    String input = "abc\n2.0\n0.5\n\n\n\n\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    
    // Δημιουργούμε το αντικείμενο με ΦΡΕΣΚΟ Scanner
    Weights weightsObj = new Weights(new Scanner(System.in));
    
    double result = weightsObj.getWeight();
    assertEquals(0.5, result, 0.001);
}

@Test
void testAddWeightsWithRetries() {
    StringBuilder sb = new StringBuilder();
    // Section Οικονομικά: 0.5 + 0.1 + 0.1 = 0.7 (Retry) -> 0.4 + 0.3 + 0.3 = 1.0 (Success)
    sb.append("0.5\n0.1\n0.1\n");
    sb.append("0.4\n0.3\n0.3\n");
    // Section Περιβαλλοντικά: 0.4 + 0.3 + 0.3 = 1.0 (Success)
    sb.append("0.4\n0.3\n0.3\n");
    // Section Κοινωνικά: 0.25 + 0.25 + 0.25 + 0.25 = 1.0 (Success)
    sb.append("0.25\n0.25\n0.25\n0.25\n");
    // Έξτρα ασφάλεια
    sb.append("\n\n\n\n");

    System.setIn(new ByteArrayInputStream(sb.toString().getBytes()));
    Weights weightsObj = new Weights(new Scanner(System.in));
    
    double[] result = weightsObj.addWeights();
    assertEquals(10, result.length);
}