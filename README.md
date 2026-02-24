# monobank-kotlin-sdk

Kotlin SDK for the [Monobank Acquiring API](https://monobank.ua/api-docs/acquiring/methods/). Covers internet acquiring, QR payments, subscriptions, wallet tokenization, split payments, and webhook verification.

## Requirements

- JVM 11+
- No framework dependencies — works with any JVM project

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("app.spilki.monobank:acquiring-sdk:0.1.0")
}
```

### Gradle (Groovy)

```groovy
dependencies {
    implementation 'app.spilki.monobank:acquiring-sdk:0.1.0'
}
```

### Maven

```xml
<dependency>
    <groupId>app.spilki.monobank</groupId>
    <artifactId>acquiring-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Quick Start

```kotlin
import app.spilki.monobank.acquiring.MonobankAcquiring
import app.spilki.monobank.acquiring.MonobankAcquiringConfig
import app.spilki.monobank.acquiring.model.invoice.CreateInvoiceRequest

val mono = MonobankAcquiring(
    MonobankAcquiringConfig(token = "your-merchant-token")
)

// Create an invoice
val invoice = mono.invoices.create(
    CreateInvoiceRequest(
        amount = 4200,      // 42.00 UAH in kopiyky
        ccy = 980,          // UAH (ISO 4217)
        redirectUrl = "https://yourshop.com/order/123/done",
        webHookUrl = "https://yourshop.com/api/monobank/webhook",
    )
)
println("Payment page: ${invoice.pageUrl}")

// Check invoice status
val status = mono.invoices.status(invoice.invoiceId)
println("Status: ${status.status}")
```

## API Coverage

| Module | Methods |
|--------|---------|
| `invoices` | create, status, cancel, remove, finalize, paymentDirect, receipt, fiscalChecks |
| `merchant` | details, publicKey, statement |
| `subscriptions` | create, status, payments, list, remove, edit |
| `qr` | details, resetAmount, list, employees |
| `wallet` | cards, payment, deleteCard |
| `split` | receivers |
| `monopay` | importPubKey, deletePubKey, listPubKeys |
| `submerchants` | list |
| `terminals` | list |
| `webhooks` | verify |

## Usage Examples

### Hold & Finalize

```kotlin
import app.spilki.monobank.acquiring.model.common.PaymentType

// Create a hold
val hold = mono.invoices.create(
    CreateInvoiceRequest(
        amount = 10000,
        ccy = 980,
        paymentType = PaymentType.HOLD,
    )
)

// Finalize for a smaller amount
mono.invoices.finalize(
    FinalizeInvoiceRequest(
        invoiceId = hold.invoiceId,
        amount = 8500,
    )
)
```

### Cancel / Refund

```kotlin
// Full refund
mono.invoices.cancel(CancelInvoiceRequest(invoiceId = "inv_abc123"))

// Partial refund
mono.invoices.cancel(
    CancelInvoiceRequest(
        invoiceId = "inv_abc123",
        amount = 2000,
    )
)
```

### Wallet Tokenization & Payment

```kotlin
// List saved cards
val cards = mono.wallet.cards(walletId = "wallet_xyz")

// Pay with a saved card token
val result = mono.wallet.payment(
    WalletPaymentRequest(
        cardToken = cards.first().cardToken,
        amount = 9900,
        ccy = 980,
    )
)
```

### Subscriptions (Recurring Payments)

```kotlin
val sub = mono.subscriptions.create(
    CreateSubscriptionRequest(
        amount = 29900,
        ccy = 980,
        interval = "1m",   // monthly
    )
)
println("Subscription page: ${sub.pageUrl}")

// Check status
val subStatus = mono.subscriptions.status(sub.subId)
```

### Webhook Verification

```kotlin
// In your webhook handler:
fun handleWebhook(xSignHeader: String, bodyBytes: ByteArray) {
    val isValid = mono.webhooks.verify(xSignHeader, bodyBytes)
    if (!isValid) {
        // reject request
        return
    }
    // process webhook payload
}
```

### QR Cash Register

```kotlin
val registers = mono.qr.list()
val details = mono.qr.details(registers.first().qrId)
```

### Statement

```kotlin
val from = Instant.parse("2025-01-01T00:00:00Z").epochSecond
val to = Instant.parse("2025-01-31T23:59:59Z").epochSecond
val items = mono.merchant.statement(from, to)
```

## Configuration

```kotlin
MonobankAcquiringConfig(
    token = "your-token",                       // required
    baseUrl = "https://api.monobank.ua",        // default
    connectTimeoutMs = 10_000,                  // default 10s
    readTimeoutMs = 30_000,                     // default 30s
    cmsName = "MyShopCMS",                      // optional
    cmsVersion = "2.1.0",                       // optional
)
```

## Error Handling

All API errors throw typed exceptions inheriting from `MonobankException`:

```kotlin
import app.spilki.monobank.acquiring.exception.*

try {
    mono.invoices.status("invalid-id")
} catch (e: MonobankAuthException) {
    // 403 — invalid token
} catch (e: MonobankRateLimitException) {
    // 429 — too many requests
} catch (e: MonobankApiException) {
    // 400 / other API errors
    println("Error: ${e.errCode} — ${e.errText}")
    println("HTTP status: ${e.statusCode}")
} catch (e: MonobankNetworkException) {
    // connection / timeout errors
}
```

## Dependencies

Runtime dependencies (minimal):
- `com.fasterxml.jackson` — JSON serialization
- `org.slf4j:slf4j-api` — logging facade

No Spring, no OkHttp, no BouncyCastle. HTTP via `java.net.http.HttpClient` (JDK 11+).

## Building

```bash
./gradlew build         # compile + test + detekt + ktlint
./gradlew test          # tests only
./gradlew detekt        # static analysis
./gradlew ktlintCheck   # code style
```

## Publishing to Maven Central

Set environment variables or `gradle.properties`:

```properties
ossrhUsername=your-sonatype-username
ossrhPassword=your-sonatype-password
signingKey=your-gpg-key
signingPassword=your-gpg-password
```

```bash
./gradlew publish
```

## License

[MIT](LICENSE)
