# Gmail Import

Import messages from a local disk in Maildir format to Gmail.

## Usage

https://www.google.com/settings/security/lesssecureapps

```
java -jar gmailimport-1.0-SNAPSHOT.jar -u guser -p gpass [-h host] [-t label] [dir|file...]
```

martin@example.com:password!host/folder

## TODO

Support more message formats

* [Mbox](http://en.wikipedia.org/wiki/Mbox) - Thunderbird
* [DBX](http://jmbox.svn.sourceforge.net/viewvc/jmbox/trunk/src/java/jmbox/oe5dbx/) - Outlook Express 5/6
* [PST](http://code.google.com/p/java-libpst/) - Outlook

