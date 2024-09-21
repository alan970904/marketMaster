<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>進貨結果</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 800px;
            margin: auto;
            background: white;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
        }
        .success-message {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
            border-radius: 4px;
            padding: 10px;
            margin-bottom: 20px;
        }
        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
            padding: 10px;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>進貨結果</h1>
        
        <% if(request.getAttribute("errorMessage") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } else { %>
            <div class="success-message">
                進貨成功！以下是進貨詳情：
            </div>
            
            <table>
                <tr>
                    <th>項目</th>
                    <th>詳情</th>
                </tr>
                <tr>
                    <td>進貨編號</td>
                    <td><%= request.getAttribute("restockId") %></td>
                </tr>
                <tr>
                    <td>員工編號</td>
                    <td><%= request.getAttribute("employeeId") %></td>
                </tr>
                <tr>
                    <td>進貨日期</td>
                    <td><%= request.getAttribute("restockDate") %></td>
                </tr>
                <tr>
                    <td>商品編號</td>
                    <td><%= request.getAttribute("productId") %></td>
                </tr>
                <tr>
                    <td>商品名稱</td>
                    <td><%= request.getAttribute("productName") %></td>
                </tr>
                <tr>
                    <td>進貨數量</td>
                    <td><%= request.getAttribute("numberOfRestock") %></td>
                </tr>
                <tr>
                    <td>商品單價</td>
                    <td><%= request.getAttribute("productPrice") %></td>
                </tr>
                <tr>
                    <td>進貨總金額</td>
                    <td><%= request.getAttribute("restockTotalPrice") %></td>
                </tr>
                <tr>
                    <td>生產日期</td>
                    <td><%= request.getAttribute("productionDate") %></td>
                </tr>
                <tr>
                    <td>到期日期</td>
                    <td><%= request.getAttribute("dueDate") %></td>
                </tr>
            </table>
        <% } %>
        
        <p>
            <a href="http://localhost:8080/ispan/RestockServlet">返回首頁</a>
        </p>
    </div>
</body>
</html>