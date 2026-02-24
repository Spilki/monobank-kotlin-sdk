# AGENTS.md — Monobank Kotlin SDK

## Project Overview

Kotlin SDK for the Monobank Acquiring API. Provides typed clients for all merchant endpoints with blocking HTTP, Jackson JSON, and ECDSA webhook verification.

## Architecture

```
ua.monobank.acquiring/
├── MonobankAcquiring.kt           — Main facade, entry point
├── MonobankAcquiringConfig.kt     — Configuration (token, URLs, timeouts)
├── exception/                     — Sealed exception hierarchy
│   ├── MonobankException.kt       — Base sealed class
│   ├── MonobankApiException.kt    — HTTP 400 / generic API errors
│   ├── MonobankAuthException.kt   — HTTP 403
│   ├── MonobankRateLimitException — HTTP 429
│   └── MonobankNetworkException   — Connection / timeout failures
├── internal/                      — Internal implementation (not public API)
│   ├── HttpTransport.kt           — java.net.http wrapper
│   └── JsonCodec.kt               — Jackson ObjectMapper singleton
├── model/                         — Request/response data classes
│   ├── common/                    — Shared types (enums, PaymentInfo, BasketItem...)
│   ├── invoice/                   — Invoice create/status/cancel/finalize DTOs
│   ├── merchant/                  — Merchant details, statement, public key
│   ├── subscription/              — Recurring payment DTOs
│   ├── qr/                        — QR cash register DTOs
│   ├── wallet/                    — Card tokenization DTOs
│   ├── split/                     — Split receiver DTOs
│   ├── monopay/                   — Monopay button DTOs
│   ├── submerchant/               — Submerchant DTOs
│   └── terminal/                  — T2P terminal DTOs
├── api/                           — API client classes (one per domain)
│   ├── InvoiceApi.kt
│   ├── MerchantApi.kt
│   ├── SubscriptionApi.kt
│   ├── QrApi.kt
│   ├── WalletApi.kt
│   ├── SplitApi.kt
│   ├── MonopayApi.kt
│   ├── SubmerchantApi.kt
│   └── TerminalApi.kt
└── webhook/
    └── WebhookVerifier.kt         — ECDSA signature verification
```

## Key Design Decisions

1. **Blocking HTTP** — Uses `java.net.http.HttpClient` synchronously. No coroutines. Keeps the SDK simple and compatible with any JVM framework.
2. **JVM 11 bytecode target** — Compiled on JVM 21 but targets JVM 11 for maximum compatibility (KillBill plugins, older services).
3. **Jackson for JSON** — Widely used, no extra dependency for most JVM projects. Kotlin module registered for data class support.
4. **Sealed exception hierarchy** — `MonobankException` is sealed. Consumers can use exhaustive `when` blocks.
5. **Internal transport** — `HttpTransport` and `JsonCodec` are `internal`. Users interact only with `MonobankAcquiring` facade and API classes.
6. **No BouncyCastle** — Webhook ECDSA verification uses standard JDK `java.security` APIs.

## API Reference

All endpoints documented at: https://monobank.ua/api-docs/acquiring/methods/

Base URL: `https://api.monobank.ua`
Auth: `X-Token` header with merchant token from https://web.monobank.ua/

## Testing

- 54 unit tests using JUnit 5 + MockWebServer + AssertJ
- Run: `./gradlew test`
- All HTTP interactions are mocked — no real API calls in tests

## Code Quality

- **detekt** — Static analysis (`./gradlew detekt`)
- **ktlint** — Code formatting (`./gradlew ktlintCheck`)
- Config: `detekt.yml`, `.editorconfig`

## How to Add a New Endpoint

1. Create request/response data classes in `model/{domain}/`
2. Add method to the appropriate `api/{Domain}Api.kt`
3. Add test in `test/.../api/{Domain}ApiTest.kt`
4. Run `./gradlew build` to verify

## Reference Implementation

The existing `spilki-monobank-plugin` at `../spilki-monobank-plugin/` is a Java KillBill payment plugin that uses a subset of these APIs. This SDK is a superset of that plugin's Monobank client.
