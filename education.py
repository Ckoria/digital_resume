import streamlit as st

def edu_info():
    return  [
        ['WeThinkCode | Software Engineering', '24 Months (2023 - 2024)', 
         'In Progress', 'https://www.wethinkcode.co.za/contact'], 
        ["Cisco | Creating Compelling Reports", "1 Months (MAR 2024)", 
         "Completed", 'https://www.credly.com/badges/2123f044-31c5-49be-8b1d-60e8e8f87225/public_url'], 
        ["Coursera (AWS)  | AWS Cloud Technical Essentials", "1 Month (MAR 2024)", 
         "Completed", "https://coursera.org/share/0dda5be89b54507260c8f52320b391bb"], 
        ["Cisco  | Data Analytics Essentials", "2 Months (FEB 2024)", 
         "Completed", "https://www.credly.com/badges/71526499-94e8-4c2a-ac14-18234435c5a9/public_url"], 
        ["Cisco  | Advanced Python (OOP)", "1 Month (FEB 2024)", 
         "Completed", "https://www.credly.com/badges/a4b286ad-26e7-439c-a166-a862298932f2/public_url"], 
        [" Coursera (The Hong Kong University of Science and Technology) | Python and Statistics for Financial Analysis",
         "1 Month (AUG 2023)", "Completed", "https://coursera.org/share/e6b7f2942b30e50c32c2dfb560db3c31"], 
        ["Coursera  | IBM Intro to Data Science", "1 Months (FEB 2022)", 
         "Completed", "https://coursera.org/share/1f4afb233376528b9c25f58aced5c229"], 
        ["University of the Witswatersrand | BSc Computer Science", " 1 year (2015)", "Discontinued", ""],
        ["Isibani Academy | Grade 12", "1 year (2014)", "Completed", ""]]

def education():
    st.subheader(":classical_building: Skills & Training")
    st.write("---")
    edu_infos = edu_info()
    xpand = st.expander(label="Education")
    for info in edu_infos:
        xpand.write("") 
        xpand.write(f":books: {info[0]}")
        xpand.write(f"⌛ Duration: {info[1]}")
        xpand.write(f"🔜 Status: {info[2]}")
        xpand.write(f"✅[Certificate Verification]({info[3]})")
        xpand.write("") 


   