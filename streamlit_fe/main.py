import streamlit as st
import re
import requests

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
    confirmValid=True
  else:
    st.error("❌ Your password and confirm password do not match")

# Input fullname
fullname = st.text_input("😊 Your fullname")


# Input email

validEmail = False

col1, col2 = st.columns([5,1])
with col1: email = st.text_input("📩 Your email")
with col2: 
  st.markdown("<br>", unsafe_allow_html=True)
  verify = st.button("Verify email")

col3, col4, col5= st.columns([1,4,1])
with col4: 
  code = st.text_input("Input code", placeholder="Verified code", label_visibility="collapsed")
with col5: 
  sendCode = st.button("Send code")

mes= st.empty()
if verify:
  if email:
    try:
      response = requests.post(
        "http://127.0.0.1:8080/email/sending",
        json={"email": email},
        headers={"Content-Type": "application/json"}
      )
      if response.status_code==200:
        mes.success(response.text)
      else: mes.warning(response.text)
    except Exception as e:
      st.error(f"Error: {e}")
  else:
    st.warning("Please input your email")

if sendCode:
  if email:
    if code:
      try:
        response2 = requests.post(
                  "http://127.0.0.1:8080/email",
                  json={"email": email, "code":code},
                  headers={"Content-Type": "application/json"}
                )
        if response2.status_code==200:
          mes.success(response2.text)
          validEmail=True
        else: mes.warning(response2.text)
      except Exception as e:
          st.error(f"Error: {e}")
    else: st.warning("Please input your code")
  else: st.warning("Please input your email")

if st.button("Register"):
  if username and password and confirmPw and fullname and email:
    if validUn and validPw and confirmValid:
      try:
        res3= requests.post(
          "http://127.0.0.1:8080/user",
          json={"username": username,
                "password": password,
                "pwConfirm": confirmPw,
                "email":email,
                "name": fullname},
          headers={"Content-Type": "application/json"}
        )
        if res3.status_code==200:
          st.success("Register successful!!")
        else: st.error(res3.text)
      except Exception as e:
        st.error(f"Error: {e}")
    else: st.error("Please check your information again")
  else: st.error("Please check your information again")

      

    




