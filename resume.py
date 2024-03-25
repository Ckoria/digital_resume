from pathlib import Path
import streamlit as st
from streamlit_javascript import st_javascript
from PIL import Image
from datetime import datetime
from time import sleep
from education import education
from skills import skillz
from jobs import *
import json
from personal_details import *

# ----- Path Configure -----
current_dir = Path(__file__).parent if "__file__" in locals() else Path.cwd()
style_file = current_dir / "style" / "style.css"


for _ in range(1, 9):
    ppic = "ppc" + str(_) + ".png"
ppic_file = current_dir / "assets" / "ppc6.png" 
logo = current_dir / "assets" / "kwc.png" 
resume_pdf = current_dir / "assets" / "resume.pdf" 
style = current_dir / "scripts" / "style.js" 
experience = current_dir / "assets" / "experience.txt"

def write_json(json_file: json, data: dict) -> None:
    with open(json_file, "w") as file:
        json.dump(data, file)
        
        
def read_json(json_file: json) -> dict:
    with open(json_file, "r") as file:
        data = json.load(file)
    return data

# ----- App Design -----

st.set_page_config(page_title=page_title, page_icon=page_icon, layout="centered")
with open(style_file) as f:
    st.markdown(f"<style>{f.read()}</style>", unsafe_allow_html=True)
    
with open(resume_pdf, "rb") as pdf_file:
    resume_file = pdf_file.read()

with open(style) as f:
    components.html(f"<script>{f.read()}</script>")
  
ppic = Image.open(ppic_file)
logo = Image.open(logo)

st.image(logo)
#  Personal Details Columns
st.title(full_name)
st.write(description)
st.download_button(
    label=" :floppy_disk: Download PDF Resume",
    data= resume_file,
    file_name=resume_pdf.name,
    mime="application/octet-stream"    
)
st.write(f" :email: {email_address}")    
    
# ----- Social Media -----
st.write("#")
social_media = social_media()
cols = st.columns(len(social_media))
for index, (platform, link) in enumerate(social_media.items()):
    cols[index].write(f"[{platform}]({link})")

# Sidebar for Reviews

# Sidebar
def ratings_sidebar():
    sidebar = st.sidebar
    with sidebar:
        st.image(ppic, width = 220)    
        # Add widgets to the sidebar
        form_2 = st.form(key='form_side')
        with form_2:
            name = st.text_input("Your name")
            ratings = st.slider("Please give your ratings ", 1, 5)
            comment = st.text_area("Please give feedback")
            submit_rating = st.form_submit_button(label='Submit Ratings')
            curr_date = datetime.now()
            reviews = [name, ratings, comment]
        if submit_rating:
            load_reviews = read_json('ratings.json')
            load_reviews[str(curr_date)[:19]] = reviews
            write_json('ratings.json', load_reviews)
            st.success("Thank you for your feedback 🙏")
    return read_json('ratings.json')

# ----- Experience & Training -----

# Section for Education

st.write("#")
education()


#  Section for Skills

st.write("---")
skillz()
st.write("#")
st.write("---")
xpand = st.expander(label="Skills Description")
with open(experience,'r+') as experience_file:
    for line in experience_file:
        xpand.write(f"- ✔️ {line}")


#  Section for Work Experience

# Work Experience 1
job_one()
# Work Experience 2
job_two()
# Work Experience 3
job_three()


# ----- All Projects -----
st.write("#")
st.subheader(":trophy: Projects  &  Achievements")
st.write("---")


#  Load Projects
projects = projects()
xpand = st.expander(label="Projects")
for index, (project, link) in enumerate(projects.items()):
    xpand.write(f":white_check_mark: [{project}]({link})")


# Load reviews
st.write("#")
st.subheader(":star: Reviews and Ratings")
st.write("---")

def reviews_section():
    reviews = ratings_sidebar() 
    for date_key, review_value in reviews.items():
        stars = "⭐" * review_value[1]
        xpand = st.expander(label=f"{review_value[0]}\n{stars}")
        xpand.write(f"""
                    {date_key} \n
                    - ✅ {review_value[-1]}
                    """)
          
reviews_section()
# ratings_sidebar()            

