from django.urls import path
from login.views import LoginView, SignUpView, FindEmailView, FindPwView, EmailSendView, EmailVerifyView

urlpatterns = [
    path('login', LoginView.as_view()),
    path('signup', SignUpView.as_view()),
    path('signup/<int:bid>/', SignUpView.as_view()),
    path('findemail', FindEmailView.as_view()),
    path('findpw', FindPwView.as_view()),
    path('mailsend', EmailSendView.as_view()),
    path('mailverify',EmailVerifyView.as_view() )
]