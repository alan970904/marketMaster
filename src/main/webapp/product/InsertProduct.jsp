<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">

    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/CSS/style.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/extra.css">

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新增商品</title>
    <style>
        .sub {
            text-align: center;
        }
        body {
            font-family: Arial, sans-serif;
        }
        .st1 {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            margin: 10px 0;
        }
        .submit {
            background-color: #4CAF50;
            border: none;
            border-radius: 5px;
            padding: 10px 20px;
            cursor: pointer;
            color: white;
        }
        .submit:hover {
            background-color: #45a049;
        }
        legend {
            font-size: 2em;
            text-align: center;

        }
        label {
            flex: 0 0 40%;
            text-align: right;
            padding-right: 10px;

        }
        input[type="text"] {
            margin-right: 100px;
            background-color: lightgrey;
            border-radius: 5px;
            padding: 5px;
        }
        fieldset {
            margin: 0 auto;
            width: 650px;
            border-radius: 10px;
            padding: 20px;
            border: 1px solid #ddd;
            text-align:center;
        }
        .error-message {
            color: #ff6b6b;
            text-align: center;
            margin-bottom: 15px;
            font-weight: bold;
        }
    </style>
</head>

<body>
    <%@ include file="/body/body.jsp" %>
    <main>
    <form method="post" action="${pageContext.request.contextPath}/ProductsServlet">
        <fieldset>
            <legend>新增商品</legend>
            <% if(request.getAttribute("errorMessage") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            <div class="st1">
                <label for="productId">輸入要新增的商品編號：</label>
                <input type="text" name="productId" id="productId" required />
            </div>
            <div class="st1">
                <label for="productCategory">輸入要新增商品類別名稱：</label>
                <input type="text" name="productCategory" id="productCategory" required />
            </div>
            <div class="st1">
                <label for="productName">輸入要新增的商品名稱：</label>
                <input type="text" name="productName" id="productName" required />
            </div>
            <div class="st1">
                <label for="productPrice">輸入要新增的商品售價：</label>
                <input type="text" name="productPrice" id="productPrice" required />
            </div>
            <div class="st1">
                <label for="productSafeInventory">輸入要新增的商品安全庫存量：</label>
                <input type="text" name="productSafeInventory" id="productSafeInventory" required />
            </div>
            <input type="hidden" name="numberOfShelve" value="0" />
            <input type="hidden" name="numberOfInventory" value="0" />
            <input type="hidden" name="numberOfSale" value="0" />
            <input type="hidden" name="numberOfExchange" value="0" />
            <input type="hidden" name="numberOfDestruction" value="0" />
            <input type="hidden" name="numberOfRemove" value="0" />
            <div class="sub">
                <input type="hidden" name="action" value="InsertProduct" />
                <input type="submit" value="確定" class="submit" />
            </div>
                   <p>
                   <p>
<input type="button" value="返回首頁" onclick="window.location.href='${pageContext.request.contextPath}/product/productHomepage.jsp'">
            
        </fieldset>
    </form>
    </main>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
</body>

</html>