from pathlib import Path
import streamlit as st
from PIL import Image
from datetime import datetime

# ----- Path Configure -----
current_dir = Path(__file__).parent if "__file__" in locals() else Path.cwd()
style_file = current_dir / "style" / "style.css"
ppic_file = current_dir / "assets" / "ppic.png" 
resume_pdf = current_dir / "assets" / "resume.pdf" 
experience = current_dir / "assets" / "experience.txt"

# ----- Page Settings -----

page_title = "Sindiso Ndlovu | Digital CV"
page_icon = ":100:"
full_name = "Sindiso Christopher Ndlovu"
description = "Full Stack Developer and Junior Data Analyst" 
email_address = "sindiso.chris@gmail.com"
social_media = {"YouTube": "https://www.youtube.com/@TX521",
                "LinkedIn": "https://www.linkedin.com/in/chris-ndlovu-020847116/",
                "Website": "https://www.tradingxposed.co.za",
                "GitHub": "https://github.com/ckoria",
                "Twitter": "https://twitter.com/tradingxposed"
                
            }

projects = {"Samsung Warranty Checker App": "https://ckoria--warranty-checker-display-2ah21s.streamlit.app/",
            "Mawox Work Management System": "https://mawox.pythonanywhere.com",
            "TradingXposed Finacial Education Website": "https://www.tradingxposed.co.za",
            "MM All Status Tracker": "https://ckoria-ha-customers-get-customers-5w5cdv.streamlit.app/",
            "Mawox Excel Inventory System  (Github Repo with Excel File)": "https://github.com/Ckoria/excel_vba_pos",
            "Mawox Google Sheets Inventory System": "https://docs.google.com/spreadsheets/d/1pw1ce5nJkPKr24XKhNC6MyzDzwRb8uiRCbPDB_WtsUU/edit#gid=1641138601",
            "Google Sheets and RepairShopr API Testing (Github Repo)": "https://github.com/Ckoria/APIs-Testing",
            "On the Move App Admin Webstite  (Credintials on requets)": "https://mmallonthemove.co.za/"
            
        }

# ----- App Design -----
st.set_page_config(page_title=page_title, page_icon=page_icon, layout="wide")
with open(style_file) as f:
    st.markdown(f"<style>{f.read()}</style>", unsafe_allow_html=True)
with open(resume_pdf, "rb") as pdf_file:
    resume_file = pdf_file.read()
ppic = Image.open(ppic_file)

pic_col, desc_col = st.columns(2, gap="small")
with pic_col:
    st.image(ppic, width=225)
with desc_col:
    st.title(full_name)
    st.write(description)
    st.download_button(
        label=" :floppy_disk: Download Resume",
        data= resume_file,
        file_name=resume_pdf.name,
        mime="application/octet-stream"    
    )
    st.write(f" :email: {email_address}")
# ----- Social Media -----
st.write("#")
cols = st.columns(len(social_media))
for index, (platform, link) in enumerate(social_media.items()):
    cols[index].write(f"[{platform}]({link})")
    
# ----- Experience & Training -----
st.write("#")
st.subheader(":classical_building: Skills & Training")
st.write("---")
xpand = st.expander(label="Education")
xpand.write("") 
xpand.write(":books: WeThinkCode | Software Engineering")
xpand.write("⌛ Duration: 24 Months (2023 - 2024)")
xpand.write("🔜 Status: In Progress")
xpand.write("")
xpand.write(":books: Cisco | Creating Compelling Reports")
xpand.write("⌛ Duration: 1 Months (MAR 2024)")
xpand.write("✅ Status: Completed")
xpand.write("")
xpand.write(":books: Coursera (AWS)  | AWS Cloud Technical Essentials")
xpand.write("⌛ Duration: 1 Month (MAR 2024)")
xpand.write("✅ Status: Completed")
xpand.write("")
xpand.write(":books: Cisco  | Data Analytics Essentials")
xpand.write("⌛ Duration: 2 Months (FEB 2024)")
xpand.write("✅ Status: Completed")
xpand.write("")
xpand.write(":books: Cisco  | Advanced Python (OOP)")
xpand.write("⌛ Duration: 1 Month (FEB 2024)")
xpand.write("✅ Status: Completed")
xpand.write("")
xpand.write(":books: Coursera (The Hong Kong University of Science and Technology) | Python and Statistics for Financial Analysis")
xpand.write("⌛ Duration: 1 Month (AUG 2023)")
xpand.write("✅ Status: Completed")
xpand.write("")
xpand.write(":books: Coursera  | IBM Intro to Data Science")
xpand.write("⌛ Duration: 1 Months (FEB 2022)")
xpand.write("✅ Status: Completed")
xpand.write("")

xpand.write(":books: University of the Witswatersrand | BSc Computer Science")
xpand.write("⌛ Duration: 1 year (2015)")
xpand.write("❌ Status: Discontinued")
xpand.write("")
xpand.write(":books: Isibani Academy | Grade 12")
xpand.write("⌛ Duration: 1 year (2014)")
xpand.write("✅ Status: Completed")
xpand.write("")
st.write("---")
xpand = st.expander(label="Hard Skills")
xpand.write(
    """
    - :u7a7a: Languages: Python, Javascript, Google App Script, VB Excel, Java (Beginner) 
    - :file_cabinet: Databases: SQLAlchemy, SQLite3, MySQL, PostgreSQL
    - :earth_africa: Web Development Technologies: Flask, Jinja2 templates, Apex.js, HTML5, CSS3, WordPress
    - :bar_chart: Data Visualisation Tools: Excel, Google Sheets, Pandas, Plotly, Apex.js, Tableau
    - :computer: Automation Tools: Python, Google App Script, Bash
    """)
st.write("#")
st.write("---")
xpand = st.expander(label="Skills Description")
with open(experience,'r+') as experience_file:
    for line in experience_file:
        xpand.write(f"- ✔️ {line}")


# ----- First Job -----
st.write("#")
st.subheader(":hammer_and_wrench: Work Experience")
st.write("---")
xpand = st.expander(label="Work Experience")
st.write(":technologist: Job Title: Web Developer, Data Analysist & Booking Agent")
st.write(":capital_abcd: Company Name: Samsung Customer Service" 
st.write(f"""
:spiral_calendar_pad: Duration: {str(datetime(2022, 1, 26))[0:11]} - {str(datetime(2023, 4 , 30))[0:11]}
""")
xpand = st.expander(label="Responsibilities")
xpand.write("""
- ✔️ Booking customers for repairs
- ✔️ Perform assessment on items brought for repairs
- ✔️ Proving advice and troubleshooting for devices with minor defects for walk in customers
- ✔️ Development of Data Management System using Python Flask Framework, HTML5, CSS, and Javascript
- ✔️ Integration of RepairShopr internal system with Samsung GSPN system to avoid using both systems for data capturing and referencing
- ✔️ Automation of pricing system for Samsung parts sales instead of adjusting pricing manually since Samsung has thousands of parts for sale
- ✔️ Designed Automated scheduler for in home repairs using Google Sheets and Google App Scripts which improved the customer service with a long shot and helped to clear a backlog from 2021  
- ✔️ Automated system for vehicles which helped with servicing vehicles on time and gas filling with a monthly budget prediction model using Python and Machine Learning frameworks with google sheets APIs for data visualization
- ✔️ Developed warranty checking tool using Samsung APIs for efficiency instead of relying on the proof of purchase which was not a reliable for warranty validation.   
- ✔️ My main role was to automate as many tasks as possible to improve performance and working process.
- ✔️ Assist with development of OnTheMove App which tracks the movement of technicians and show their live location.
""")
xpand = st.expander(label="Reference")
xpand.write("""
- :handshake: Vinod
- :office_worker: Project Manager
- :telephone_receiver: (+91)96 626 61991
""")

# ----- Second Job -----
st.write(":technologist: Job Title: Systems & Web Developer")
st.write(":capital_abcd: Company Name: Mawox Business Hub")
st.write(f"""
:spiral_calendar_pad: Duration: {str(datetime(2018, 2, 1))[0:11]} - {str(datetime(2021, 12 , 27))[0:11]}
""")
xpand = st.expander(label="Responsibilities")
xpand.write("""
- ✔️ Data capturing
- ✔️ Data Analysis for business performance insights using Excel and Google Sheets
- ✔️ Development of Point of Sales system using Excel and VB
- ✔️ Development of Business Management System using Python Flask Framework, HTML, CSS and JavaScript
- 
""")
xpand = st.expander(label="Reference")
xpand.write("""
- :handshake: Nathi PJ Mlaba
- :office_worker: Founder and CEO
- :telephone_receiver: (+27)78 339 2740
""")

# ----- Third Job -----
st.write(":technologist: Job Title: Unit Testing & Debugging")
st.write(":capital_abcd: Company Name: Vinod | Remote")
st.write(f"""
         :spiral_calendar_pad: Duration: {str(datetime(2022, 8, 9))[0:11]} - {str(datetime(2023, 2, 28))[0:11]}
""")
xpand = st.expander(label="Responsibilities")
xpand.write("""
- ✔️ Performing unit tests for On the Move mobile application
- ✔️ Testing the performance of the Admin dashboard and suggest improvements
- ✔️ Ensure that milestones are met and all functionalities are implemented at optimal performance 
- ✔️ Adding styling on visual appearance of the admin dashboard using css, javascript and bootstrap
""")
xpand = st.expander(label="References")
xpand.write("""
- :handshake: Vinod
- :office_worker: Project Manager
- :telephone_receiver: (+91)96 626 61991 
""")

# ----- All Projects -----
st.write("#")
st.subheader(":trophy: Projects  &  Achievements")
st.write("---")
for index, (project, link) in enumerate(projects.items()):
    st.write(f":white_check_mark: [{project}]({link})")
