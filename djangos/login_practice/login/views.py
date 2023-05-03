from django.shortcuts import render
import json, re, traceback
from django.http            import JsonResponse  
from django.views           import View          
from django.core.exceptions import ValidationError
from django.db.models       import Q                                                                                                                
from .models                import User
from django.views.decorators.csrf import csrf_exempt


class LoginView(View):
     @csrf_exempt
     def post(self, request):
        data = json.loads(request.body)

        try:
            request_id = data.get('id')
            request_password = data.get('pw')

            # 입력한 값이 Email인지 핸드폰 번호인지 검사
            id = User.objects.filter(user_id = request_id)
            if id.exists():
                account = User.objects.get(user_id = request_id)
                if account.password == request_password:
                    return JsonResponse({'message' : 'SUCCESS'}, status=200)
                else:
                    return JsonResponse({"message": "INVALID_PASSWORD"}, status=401)

            # ID 틀렸을시 return    
            return JsonResponse({"message": "INVALID_USER"}, status=401)
        # 다른 값을 입력했을시 return
        except KeyError: 
            return JsonResponse({"message": "KEY_ERROR"}, status=400)