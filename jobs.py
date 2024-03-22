import streamlit as st
from datetime import datetime

def job_one():
    # ----- First Job -----
    st.write("#")
    st.subheader(":hammer_and_wrench: Work Experience")
    st.write("---")
    xpand = st.expander(label="Work Experience")
    st.write(":technologist: Job Title: Web Developer & App Tester")
    st.write(":capital_abcd: Company Name: Samsung Customer Service (12 Months Contract) ") 
    st.write(f"""
    :spiral_calendar_pad: Duration: {str(datetime(2022, 1, 26))[0:11]} - {str(datetime(2023, 4 , 30))[0:11]}
    """)
    xpand = st.expander(label="Responsibilities")
    xpand.write("") 
    xpand.write("""
    - ✔️ Development of Data Management System using Python Flask Framework, HTML5, CSS, and Javascript
    - ✔️ Integration of RepairShopr internal system with Samsung GSPN system to avoid using both systems for data capturing and referencing
    - ✔️ Automation of pricing system for Samsung parts sales instead of adjusting pricing manually since Samsung has thousands of parts for sale
    - ✔️ Designed an Automated scheduler for in-home repairs using Google Sheets and Google App Scripts which improved customer service with a long shot and helped to clear a backlog from 2021  
    - ✔️ Automated system for vehicles which helped with servicing vehicles on time and gas filling with a monthly budget prediction model using Python with Google Sheets APIs for data visualization
    - ✔️ Developed warranty checking tool using Samsung APIs for efficiency instead of relying on the proof of purchase which was not reliable for warranty validation.   
    - ✔️ My main role was to automate as many tasks as possible to improve performance and working processes.
    - ✔️ Assist with the development of the OnTheMove App which tracks the movement of technicians and shows their live location.
    """)
    xpand.write("") 
    xpand = st.expander(label="Reference")
    xpand.write("") 
    xpand.write("""
    - :handshake: Vinod
    - :office_worker: Project Manager
    - :telephone_receiver: (+91)96 626 61991
    """)
    xpand.write("") 

def job_two():
    # ----- Second Job -----
    st.write(":technologist: Job Title: Systems & Web Developer")
    st.write(":capital_abcd: Company Name: Mawox Business Hub")
    st.write(f"""
    :spiral_calendar_pad: Duration: {str(datetime(2018, 2, 1))[0:11]} - {str(datetime(2021, 12 , 27))[0:11]}
    """)
    xpand = st.expander(label="Responsibilities")
    xpand.write("""
    - ✔️ Data migration, Excel to SQLite3
    - ✔️ Data Analysis for business performance insights using Python, Excel, Tableau, and Google Sheets
    - ✔️ Development of Point of Sales system using Excel and VB
    - ✔️ Development of Customer Management System (Web App) using Python Flask Framework, HTML, CSS, and JavaScript
    - 
    """)
    xpand = st.expander(label="Reference")
    xpand.write("""
    - :handshake: Nathi PJ Mlaba
    - :office_worker: Founder and CEO
    - :telephone_receiver: (+27)78 339 2740
    """)


def job_three():
    # ----- Third Job -----
    st.write(":technologist: Job Title: App Testing & Debugging")
    st.write(":capital_abcd: Company Name: Vinod | Remote")
    st.write(f"""
            :spiral_calendar_pad: Duration: {str(datetime(2022, 8, 9))[0:11]} - {str(datetime(2023, 2, 28))[0:11]}
    """)
    xpand = st.expander(label="Responsibilities")
    xpand.write("""
    - ✔️ Performing unit tests for On the Move mobile application
    - ✔️ Testing the performance of the Admin dashboard and suggest improvements
    - ✔️ Ensure that milestones are met and all functionalities are implemented at optimal performance 
    - ✔️ Adding styling on the visual appearance of the admin dashboard using CSS, javascript and Bootstrap
    """)
    xpand = st.expander(label="References")
    xpand.write("""
    - :handshake: Vinod
    - :office_worker: Project Manager
    - :telephone_receiver: (+91)96 626 61991 
    """)