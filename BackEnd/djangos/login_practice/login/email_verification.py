import smtplib
from email.mime.text import MIMEText

def email_validate(reciever_email, code):
    sender_email = "commitverify@gmail.com"
    reciever_email = "qnf323@naver.com"
    smtp = smtplib.SMTP('smtp.gmail.com', 587)
    smtp.starttls()  # TLS 사용시 필요

    smtp.login(user=sender_email, password="laziyhourwqixidf")
    
    msg = MIMEText(f"Cafe Oasis에서 보낸 인증 코드입니다.\n{code} 를 입력해주세요 ")#내용
    msg['From'] = sender_email #보내는 사람
    msg['Subject'] = 'Cafe Oasis 인증 메일' #제목
    msg['To'] = reciever_email#받는 사람
    smtp.sendmail(from_addr="commitverify@gmail.com", to_addrs=reciever_email, msg = msg.as_string())
    
    smtp.quit()