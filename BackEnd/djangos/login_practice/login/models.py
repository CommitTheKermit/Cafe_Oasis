from django.db import models

# Create your models here.

class User(models.Model):
    user_id = models.CharField(max_length=20)
    email = models.CharField(max_length=20)
    password = models.CharField(max_length=20)
    
    def __str__(self):
        return f'{self.user_id}, {self.password}'
    
    class Meta:
        db_table = 'users_custom'