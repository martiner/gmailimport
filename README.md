# Gmail Import

Import messages from a local disk in Maildir format to Gmail.

## Usage

### List
```
java -jar target/gmailimport-1.0-SNAPSHOT.jar -u guser -p gpass [-l label]
```

### Import
```
java -jar target/gmailimport-1.0-SNAPSHOT.jar -u guser -p gpass [-l label] -i [maildir...]
```

## TODO

Support more message formats

* [Mbox](http://en.wikipedia.org/wiki/Mbox) - Thunderbird
* [DBX](http://jmbox.svn.sourceforge.net/viewvc/jmbox/trunk/src/java/jmbox/oe5dbx/) - Outlook Express 5/6
* [PST](http://code.google.com/p/java-libpst/) - Outlook
