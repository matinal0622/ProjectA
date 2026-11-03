# ProjectA

三人协作代码仓库。`main` 保存团队确认后的代码；每人使用自己的任务分支，通过 Pull Request（PR）合并。

## 加入团队

仓库所有者在 Settings → Collaborators → Add people 邀请另外两位成员。成员接受邀请后才能向本仓库推送分支。每个人使用自己的 GitHub 账号。

## 第一次获取项目

```bash
git clone https://github.com/matinal0622/ProjectA.git
cd ProjectA
```

也可以使用 GitHub Desktop：File → Clone repository → URL，输入上面的仓库地址。使用已接受邀请的账号登录。

## 每次开发流程

1. 在 Issues 创建任务，写清目标并指派负责人，避免重复修改同一功能。
2. 从最新 main 创建任务分支，将下面的 `yourname` 和 `task-name` 换成自己的名字和任务名。

```bash
git switch main
git pull --ff-only origin main
git switch -c feature/yourname-task-name
```

3. 编写代码并在本地运行检查；用 `git status` 和 `git diff` 确认要上传的内容。

```bash
git status
git diff
git add path/to/changed-file
git commit -m "Describe the change"
git push -u origin HEAD
```

`path/to/changed-file` 是占位符，请替换为实际改动文件路径。

4. 在 GitHub 点击 Compare & pull request，目标分支选择 `main`，说明改动和测试方法，请另一位成员检查。
5. 检查通过、冲突解决后合并 PR。其他成员切回 main 并执行 `git pull --ff-only origin main` 获取更新，再开始下一项任务。

GitHub Desktop 对应操作：Fetch origin / Pull origin → New branch → 修改文件 → Commit → Publish branch / Push origin → Create Pull Request。

## 使用网页上传代码

在 Code 页的分支菜单中，从 main 创建自己的任务分支 → Add file → Upload files → 上传源代码并提交到该分支 → 创建 PR 合并到 main。不要上传整个压缩包代替可阅读的源代码。

## 团队约定

- 一个任务一个分支、一个 PR；提交说明写清楚修改目的。
- 合并前由另一位成员检查，并在 PR 中记录实际运行过的检查结果。
- 开始工作前先同步 main；有未提交修改时先提交或妥善保存。
- 遇到推送被拒绝或合并冲突，先同步并协调修改；不要使用 force push 覆盖队友提交。
- 不提交密码、访问令牌、真实凭据、个人配置或编译产物。当前仓库为公开仓库，提交内容会公开可见。
- 代码加入后，在本文件补充实际使用的语言、运行环境、启动命令和测试命令。
