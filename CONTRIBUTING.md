# Contributing to Prithvi

Thank you for considering contributing to **Prithvi** – a custom in-memory key-value database with deep system design!

We welcome contributions of all kinds – features, bug fixes, documentation, or ideas. Follow this guide to get started quickly and contribute effectively.

---

## Local Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/psidh/prithvi.git
   cd prithvi
   ```

````bash

2. Build and run:

   ```bash
   javac Main.java
   java Main
````

3. (Optional) Install JS or Python SDK:

   - Check out: [`prithvi-js-sdk`](https://github.com/psidh/prithvi-js-sdk)
   - Check out: [`prithvi-py-sdk`](https://github.com/psidh/prithvi-py-sdk)

---

## Git Workflow

We follow **Trunk-Based Development** to maintain a stable and fast-moving mainline.

- All changes go through **pull requests** (PRs).
- Work on short-lived branches off `main`:
  `feature/xyz`, `bugfix/login-issue`, etc.
- Regularly pull updates from `main`.
- Once your feature is ready, push and open a PR.

---

## Branch Protection Rules

> Direct pushes to `main` are **NOT** allowed.

To ensure code quality and stability:

- Pull Requests must:

  - Be reviewed by at least **1 maintainer**
  - **Pass all status checks** (e.g., tests/build)
  - Be **squash merged** (linear history only)

---

## Code Style

- Java: Follow standard Java conventions.
- Write clean, readable, and modular code.
- Follow camelcase for variable names
- Use interfaces to enforce DRY principle

> Use `synchronized` wisely and write thread-safe code for core data paths.

---

## Commit Format

Use meaningful commit messages:

```bash
feat: Add RPUSH command for list support
fix: Prevent null pointer on KEYS
refactor: Extract cache eviction logic
```

Squash commits before merging for a clean history.

---

## Testing

- Add tests where applicable.
- Manual testing is encouraged for concurrency and TTL behavior.
- Log outputs should be clear and concise.

---

## Design Philosophy

Prithvi is designed to be:

- Minimal yet powerful
- Transparent and easy to extend
- Resilient under concurrent load

Follow existing patterns unless there's a good reason not to.

---

## Need Help?

Open an issue or ping us on [X/Twitter](https://x.com/p_s1dharth). We're happy to support contributors.
