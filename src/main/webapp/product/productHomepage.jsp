<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<link rel="stylesheet"
href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
 src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>

<!-- Bootstrap CSS -->
<link
 href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
 rel="stylesheet">

<!-- DataTables CSS -->
<link rel="stylesheet"
 href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">

<!-- Font Awesome -->
<link
 href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css"
 rel="stylesheet">

<!-- Google Fonts -->
<link
 href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap"
 rel="stylesheet">

<!-- Custom CSS -->
<link href="/ispan/CSS/style.css" rel="stylesheet">

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<!-- Bootstrap JS Bundle with Popper -->
<script
 src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- DataTables JS -->
<script
 src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
<script
 src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
 
<link rel="stylesheet" href="/ispan/CSS/extra.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品資料處理</title>
    <style>
        .home h2 {
            margin: 5px 0;
            text-align: center;
        }

        .home {
            width: 350px;
            border: 1px solid mediumpurple;
            border-radius: 15px;
            padding: 15px;
            margin: 30px auto;
            text-align: center;
        }

        .homecontent {
            margin-bottom: 15px;
        }

        .home button,.home input[type="submit"] {
            border: 0;
            background-color: #003C9D;
            color: #fff;
            border-radius: 5px;
            padding: 10px 15px;
            cursor: pointer;
            font-size: 1em;
        }

         .home button:hover,.home input[type="submit"]:hover {
            color: #003C9D;
            background-color: #fff;
            border: 1px solid #003C9D;
        }

       .home input[type="text"] {
            background-color: lightgrey;
            border-radius: 5px;
            padding: 5px;
            width: calc(100% - 12px); /* Full width minus padding */
            margin-top: 5px;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<%@ include file="/body/body.jsp" %>
<main>

<div class="home">
    <h2>商品資料處理</h2>
    
    <form method="get" action="${pageContext.request.contextPath}/ProductsServlet">
        <div class="homecontent">
            <input type="hidden" name="action" value="GetPagesProducts">
            <input type="submit" id="addButton" value="取得所有資料">
        </div>
    </form>
    <div class="homecontent">
        <button onclick="window.location.href='${pageContext.request.contextPath}/product/InsertProduct.jsp'">新增商品資料</button>
    </div>

</div>
</main>
</body>
</html>
