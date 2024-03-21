from pathlib import Path
import streamlit as st
from PIL import Image
from datetime import datetime
from time import sleep
from education import education
from skills import skillz
from jobs import *
from personal_details import *

# ----- Path Configure -----
current_dir = Path(__file__).parent if "__file__" in locals() else Path.cwd()
style_file = current_dir / "style" / "style.css"

for _ in range(1, 9):
    ppic = "ppc" + str(_) + ".png"
    ppic_file = current_dir / "assets" / ppic 
resume_pdf = current_dir / "assets" / "resume.pdf" 
experience = current_dir / "assets" / "experience.txt"


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
social_media = social_media()
cols = st.columns(len(social_media))
for index, (platform, link) in enumerate(social_media.items()):
    cols[index].write(f"[{platform}]({link})")


    
# ----- Experience & Training -----
st.write("#")
education()

st.write("---")
skillz()

st.write("#")

st.write("---")
xpand = st.expander(label="Skills Description")
with open(experience,'r+') as experience_file:
    for line in experience_file:
        xpand.write(f"- ✔️ {line}")

job_one()
job_two()
job_three()


# ----- All Projects -----
st.write("#")
st.subheader(":trophy: Projects  &  Achievements")
st.write("---")
projects = projects()
for index, (project, link) in enumerate(projects.items()):
    st.write(f":white_check_mark: [{project}]({link})")
