import streamlit as st
from datetime import datetime

def job_one():
    # ----- First Job -----
    st.write("#")
    st.subheader(":hammer_and_wrench: Work Experience")
    st.write("---")
    xpand = st.expander(label="Work Experience")
    st.write(":technologist: Job Title: Bootcamp Assistant")
    st.write(":capital_abcd: Company Name: WeThinkCode ") 
    st.write(f"""
    :spiral_calendar_pad: Duration: {str(datetime(2024, 5, 26))[0:11]} - 
    """)
    xpand = st.expander(label="Responsibilities")
    xpand.write("") 
    xpand.write("""
    - ✔️ Assist with queries regarding boot camp process 
    - ✔️ Assist with technical issues, setting LMS using Linux commands, and programming environment.  
    - ✔️ Assistance with coding in python
    - ✔️ Setting up linux system installing network drivers, etc.

    """)
    xpand.write("") 
    xpand = st.expander(label="Reference")
    xpand.write("") 
    xpand.write("""
    - :handshake: Zenani Zwane
    - :office_worker: Campus Team Manager
    - :telephone_receiver: (+27)10 626 61991
    """)
    xpand.write("") 

def job_two():
    # ----- Second Job -----
    st.write(":technologist: Job Title: Systems & Web Developer")
    st.write(":capital_abcd: Company Name: Mawox Business Hub")
    st.write(f"""
    :spiral_calendar_pad: Duration: {str(datetime(2018, 2, 1))[0:11]} - 
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
    - :handshake: Nathi Pieter-John
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
    - ✔️ Performing quality assurance tests for on the Move mobile application
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
