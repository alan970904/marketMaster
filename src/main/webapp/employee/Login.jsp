<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>員工登入 - 生鮮超市後台管理系統</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">

    <!-- Favicon -->
    <link href="img/favicon.ico" rel="icon">

    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@400;500;700&display=swap" rel="stylesheet">
    
    <!-- Icon Font Stylesheet -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

    <!-- Customized Bootstrap Stylesheet -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <style>
        body {
            font-family: 'Noto Sans TC', sans-serif;
            background: linear-gradient(120deg, #84fab0 0%, #8fd3f4 100%);
        }
        .login-container {
            background-color: rgba(255, 255, 255, 0.9);
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
        }
        .login-form input {
            border: none;
            border-bottom: 2px solid #ddd;
            border-radius: 0;
            padding: 10px 5px;
            transition: all 0.3s ease;
        }
        .login-form input:focus {
            border-color: #84fab0;
            box-shadow: none;
        }
        .btn-login {
            background: linear-gradient(120deg, #84fab0 0%, #8fd3f4 100%);
            border: none;
            color: white;
            font-weight: bold;
            transition: all 0.3s ease;
        }
        .btn-login:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }
        .login-logo {
            font-size: 2.5rem;
            color: #84fab0;
        }
    </style>
</head>

<body class="d-flex align-items-center min-vh-100">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="login-container p-5">
                    <div class="text-center mb-4">
                        <i class="fas fa-leaf login-logo"></i>
                        <h2 class="mt-2">生鮮超市</h2>
                        <p class="text-muted">員工後台管理系統</p>
                    </div>
                    <form action="${pageContext.request.contextPath}/LoginServlet" method="post" class="login-form">
                        <div class="mb-4">
                            <input type="text" class="form-control" id="employeeId" name="employeeId" placeholder="請輸入員工編號" required>
                        </div>
                        <div class="mb-4">
                            <input type="password" class="form-control" id="password" name="password" placeholder="請輸入密碼" required>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" id="rememberMe">
                                <label class="form-check-label" for="rememberMe">記住我</label>
                            </div>
                            <a href="${pageContext.request.contextPath}/employee/ChangePassword.jsp" class="text-muted">忘記密碼？</a>
                        </div>
                        <button type="submit" class="btn btn-login w-100 py-2">登入</button>
                    </form>
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger mt-3" role="alert">
                            ${errorMessage}
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript Libraries -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>