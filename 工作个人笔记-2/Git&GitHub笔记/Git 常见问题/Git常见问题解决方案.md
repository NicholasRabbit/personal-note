#### 1, idea的gitingore文件不起作用解决办法

直接删了.idea和*.iml文，然后把.gitignore文件放到目录下重新打开项目

#### 2, Github链接不上

error:

```txt
ssh: connect to host github.com port 22: Connection timed out
fatal: Could not read from remote repository.
```

Solution: <a href="https://stackoverflow.com/questions/15589682/ssh-connect-to-host-github-com-port-22-connection-timed-out">StackOverflow</a>

For me none of the suggested solutions worked so I tried to fix it myself and I resolved it. I was getting this error on my AWS EC2 UBUNTU instance. I edited the ssh config (or add it if it does not exist).

```
nano ~/.ssh/config
```

And I added the following

```
Host github.com
 Hostname ssh.github.com
 Port 443
```

Then, run the command `ssh -T git@github.com` to confirm if the issue is fixed.

According to [this](https://help.github.com/articles/using-ssh-over-the-https-port/)

> Sometimes, firewalls refuse to allow SSH connections entirely. If using HTTPS cloning with credential caching is not an option, you can attempt to clone using an SSH connection made over the HTTPS port. Most firewall rules should allow this, but proxy servers may interfere

Hopefully this helps anyone else who's having the same issue I did.