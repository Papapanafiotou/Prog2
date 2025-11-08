import fitz
import csv
import re

pdf_path = r"Prog2\statewallet\src\main\sources\budget2025.pdf"
csv_path = r"Prog2\statewallet\src\main\sources\ministries.csv"
csv_path2 = r"Prog2\statewallet\src\main\sources\income.csv"
csv_path3 = r"Prog2\statewallet\src\main\sources\expenses.csv"


#################
# ΕΞΑΓΩΓΗ ΕΣΟΔΩΝ
#################
print("income.csv is being made")
pattern = re.compile(r"(\d{2,}\.)\s+(.*?)\s+([\d\.]+)", re.DOTALL)

with fitz.open(pdf_path) as doc, open(csv_path2, "w", newline="", encoding="utf-8") as csvfile:
    writer = csv.writer(csvfile)
    writer.writerow(["Κωδικός", "Τύπος Εσόδου","Ποσό"]) 
    pageindex = 0
    full_text = ""
    page = doc[1]
    full_text += page.get_text("text") + "\n"
    income_text_block = full_text.split("1. ΕΣΟΔΑ")[1]
    income_text_block = income_text_block.split("2. ΕΞΟΔΑ")[0]

    for match in pattern.findall(income_text_block):
        code = match[0]
        description = match[1]
        amount = match[2]
        clean_code = code.strip().replace('.', '')
        clean_desc = " ".join(description.strip().split()).replace('»', '').strip()
        clean_amount = amount.strip().replace('.', '')
        writer.writerow([clean_code, clean_desc, clean_amount])

print("income.csv is made successfully")

#####################
# ΕΞΑΓΩΓΗ ΥΠΟΥΡΓΕΙΩΝ
#####################

print("ministries.csv is being made")
pattern = re.compile(r'(\d{4,})\s+((?:Υπουργ|Αποκεντρ|Προεδρ|Βουλή|Σύνολο).*?)\s+([\d\.]+)\s+([\d\.]+)\s+([\d\.]+)',re.DOTALL)

with fitz.open(pdf_path) as doc, open(csv_path, "w", newline="", encoding="utf-8") as csvfile:
    writer = csv.writer(csvfile)
    writer.writerow(["Κωδικός", "Φορέας", "Τακτικός Προϋπολογισμός", "ΠΔΕ", "Σύνολο"])
    pageindex = 0
    full_text = ""
    page = doc[2]
    full_text += page.get_text("text") + "\n"

    for match in pattern.findall(full_text):
        code = match[0]
        

        description = ' '.join(match[1].split()) 
        
        amount1 = match[2].replace('.','')
        amount2 = match[3].replace('.','')
        amount3 = match[4].replace('.','')
        

        writer.writerow([code, description, amount1, amount2, amount3])

print("ministries.csv is made successfully")