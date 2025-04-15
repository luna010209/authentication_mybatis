import streamlit as st
import re


st.title("🌟 Welcome to Luna's universe 💪")

# Input username
patternUsername = r'^(?=.*[a-z])(?=.*\d).{4,10}$'
username = st.text_input("👤 Your username")
validUn=False

if username:
  if re.match(patternUsername, username):
    st.success("✅ Your username is valid")
    validUn=True
  else:
    st.error("❌ Username must be 4-10 characters, include at least 1 lowercase and 1 number.")

# Input Password
patternPw = r'^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[\W_]).{6,15}$'
password = st.text_input("🔒 Your password", type="password")
validPw = False

if password:
  if re.match(patternPw, password):
    st.success("✅ Your password is valid")
    validPw=True
  else:
    st.error("❌ Password must be 6-15 characters, include at least " \
    "1 uppercase, 1 lowercase, 1 number and 1 special character.")

confirmPw = st.text_input("🛡️ Confirm password", type="password")
confirmValid = False

if confirmPw:
  if confirmPw == password:
    st.success("✅ Confirm password is valid")
  else:
    st.error("❌ Your password and confirm password do not match")

# Input fullname
fullname = st.text_input("😊 Your fullname")

# Input email
import time
from math import floor

patternEmail = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
email = st.text_input("📩 Your email")

validEmail = False
col1, col2, col3 = st.columns([1,3,1])
with col1: verify = st.button("Verify email")
  
if email:
  if verify:
    with col2: code = st.text_input("", placeholder="Verified code", label_visibility="collapsed")
    with col3: time 
    




