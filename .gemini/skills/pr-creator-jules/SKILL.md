---
name: pr-creator-jules
description:
  Use this skill when Jules is asked to create a pull request (PR). It ensures all PRs
  follow the repository's established templates, standards, and safety protocols
  using Jules' native PR creation capabilities.
---

# Pull Request Creator for Jules

This skill guides Jules in creating high-quality Pull Requests that adhere to the
repository's standards and enforces strict safety rails for branch management.

## Workflow

Follow these steps strictly to create a Pull Request:

### 1. Branch Management (CRITICAL)
**CRITICAL SAFETY RAIL:** Ensure you are NOT working on the `main` or `master` branch.
- Verify the current branch.
- If the current branch is `main` or `master`, you MUST create and switch to a new descriptive branch before making or committing any changes:
  ```bash
  git checkout -b <new-branch-name>
  ```

### 2. Commit Changes
Verify that all intended changes are properly staged and committed.
- Check for unstaged or uncommitted changes.
- If there are uncommitted changes, stage and commit them with a descriptive message following Conventional Commits format (`type(scope): description`).
- **NEVER** commit directly to `main` or `master`.

### 3. Locate Template
Search for a pull request template in the repository to ensure compliance.
- Check `.github/PULL_REQUEST_TEMPLATE.md`
- Check `.github/pull_request_template.md`
- If multiple templates exist, select the most appropriate one based on the context (e.g., bug fix vs feature).

### 4. Read Template
Read the content of the identified template file entirely.

### 5. Draft Description
Create a PR description that strictly follows the template's structure.
- **Headings**: Keep all headings exactly as they appear in the template.
- **Checklists**: Review each item. Mark with `[x]` if completed. If an item is not applicable, leave it as `[ ]`. Do not check boxes for tasks you haven't verified.
- **Content**: Fill in the sections with clear, concise summaries of the changes made, focusing on the *why* and *what*.
- **Related Issues**: Link any issues fixed or related to this PR (e.g., "Closes #4").

### 6. Push Branch
Push the current branch to the remote repository.
**CRITICAL SAFETY RAIL:** Double-check your branch name before pushing. NEVER push if the current branch is `main` or `master`.

### 7. Create PR
Use your native PR creation capability to finalize the task.
- **Title**: Ensure the title follows the [Conventional Commits](https://www.conventionalcommits.org/) format (e.g., `feat: Add user management`, `fix(api): Resolve data type mismatch`).
- **Body**: Use the drafted description from Step 5.

## Principles
- **Safety First**: NEVER commit or push to `main` or `master`. This is your absolute highest priority. Always branch off first.
- **Compliance**: Never ignore the PR template. It exists to maintain repository quality.
- **Completeness**: Fill out all relevant sections thoroughly.
- **Accuracy**: Do not check boxes for tasks or checks you haven't actually performed.
