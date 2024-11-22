# Springboot_call_coindesk_api_demo

## 實作內容(共7支 API)
- 幣別DB維護功能。

```bash
1.查詢所有幣別
GET http://localhost:8080/api/currencies
request body 格式: 無
response body 格式:
[
    {
        "updatedISOTime": "2024/11/21 15:54:55",
        "currencies": [
            {
                "currencyCode": "USD",
                "chineseName": "美元",
                "rate": "96,615.772",
                "updatedTime": "2024/11/21 23:55:00"
            },
            {
                "currencyCode": "GBP",
                "chineseName": "英鎊",
                "rate": "76,453.993",
                "updatedTime": "2024/11/21 23:55:00"
            },
            {
                "currencyCode": "EUR",
                "chineseName": "歐元",
                "rate": "91,707.691",
                "updatedTime": "2024/11/21 23:55:00"
            }
        ]
    }
]
```

```bash
2.查詢特定幣別
GET http://localhost:8080/api/currencies/{currencyCode}
request body 格式: 無
response body 格式:
{
    "updatedISOTime": "2024/11/21 15:54:55",
    "currencies": [
        {
            "currencyCode": "USD",
            "chineseName": "美元",
            "rate": "96,615.772",
            "updatedTime": "2024/11/21 23:55:00"
        }
    ]
}
```

```bash
3.新增幣別相關資訊
POST http://localhost:8080/api/currencies
request body 格式:
{
    "currencies": [
        {
            "currencyCode": "CCC",
            "chineseName": "測試元",
            "rate": "30.385"
        }
    ]
}
response body 格式:
{
    "updatedISOTime": "2024/11/21 23:55:16",
    "currencies": [
        {
            "currencyCode": "CCC",
            "chineseName": "測試元",
            "rate": "30.385",
            "updatedTime": "2024/11/21 23:55:16"
        }
    ]
}
```

```bash
4.更新幣別相關資訊
PUT http://localhost:8080/api/currencies/{currencyCode}
request body 格式: 
{
    "currencies": [
        {
            "chineseName": "測試",
            "rate": "30.3855"
        }
    ]
}
response body 格式:
{
    "updatedISOTime": "2024/11/21 23:55:16",
    "currencies": [
        {
            "currencyCode": "CCC",
            "chineseName": "測試",
            "rate": "30.3855",
            "updatedTime": "2024/11/21 23:55:25"
        }
    ]
}
```

```bash
5.刪除幣別相關資訊
DELETE http://localhost:8080/api/currencies/{currencyCode}
request body 格式: 無
response body 格式: 無

```

- 呼叫coindesk的API。
```bash
6.呼叫 coinDesk 的API
GET http://localhost:8080/api/coinDesk/currentPrice
request body 格式: 無
response body 格式:
{
    "time": {
        "updated": "Nov 21, 2024 15:54:55 UTC",
        "updatedISO": "2024-11-21T15:54:55+00:00",
        "updateduk": "Nov 21, 2024 at 15:54 GMT"
    },
    "disclaimer": "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org",
    "chartName": "Bitcoin",
    "bpi": {
        "USD": {
            "code": "USD",
            "symbol": "&#36;",
            "rate": "96,615.772",
            "description": "United States Dollar",
            "rate_float": 96615.7722
        },
        "GBP": {
            "code": "GBP",
            "symbol": "&pound;",
            "rate": "76,453.993",
            "description": "British Pound Sterling",
            "rate_float": 76453.9929
        },
        "EUR": {
            "code": "EUR",
            "symbol": "&euro;",
            "rate": "91,707.691",
            "description": "Euro",
            "rate_float": 91707.691
        }
    }
}
```

- 呼叫coindesk的API，並進行資料轉換，組成新API。
```bash
7.呼叫coinDesk的API並進行資料轉換組成新API
GET http://localhost:8080/api/coinDesk/currentPrice/transToNewApi
request body 格式: 無
response body 格式:
{
    "updatedISOTime": "2024/11/21 15:54:55",
    "currencies": [
        {
            "currencyCode": "USD",
            "chineseName": "美元",
            "rate": "96,615.772",
            "updatedTime": "2024/11/21 23:55:00"
        },
        {
            "currencyCode": "GBP",
            "chineseName": "英鎊",
            "rate": "76,453.993",
            "updatedTime": "2024/11/21 23:55:00"
        },
        {
            "currencyCode": "EUR",
            "chineseName": "歐元",
            "rate": "91,707.691",
            "updatedTime": "2024/11/21 23:55:00"
        }
    ]
}
```

- H2 Console 測試
```bash
網址 http://localhost:8080/h2-console/
帳號 sa
密碼 123
```