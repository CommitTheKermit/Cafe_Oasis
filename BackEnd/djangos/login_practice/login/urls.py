from django.urls import path
from login.views import LoginView

urlpatterns = [
    path('login', LoginView.as_view()),
]