from django.db import models

# Create your models here.

class User(models.Model):
    user_id = models.AutoField(primary_key=True)
    user_email = models.EmailField(max_length=255, default='none')
    user_pw = models.CharField(max_length=60)
    user_name = models.CharField(max_length=255)
    user_phone = models.CharField(max_length=20)
    user_registration_date = models.DateField(auto_now=True)
    user_type = models.SmallIntegerField()
    
    def __str__(self):
        return f'{self.user_id}, {self.user_email}'
    
    class Meta:
        db_table = 'users_Oasis'

class EmailCode(models.Model):
    user_email = models.EmailField(max_length=255, default='none')
    user_code = models.CharField(max_length=6)

    class Meta:
        db_table = 'email_code_Oasis'