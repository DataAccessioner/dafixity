**Notice:** *As of the beginning of 2023 the Data Accessioner project no longer has any active developers maintaining it.*

# DAFixity:  Data Accessioner Fixity Checker

DAFixity is a simple file fixity checker designed to work with the
Data Accessioner tool.  It parses the report generated by Data
Accessioner, creates a list of accessioned files and their
checksums, then validates those checksums against the accessioned
files on disk.  It produces a detailed log, as well as a CSV report.

## Build prerequisites

  * A recent version of Java (>= 1.8)
  * Maven (>= 3.0)

## Build

DAFixity is built using Maven.  To build, clone the repository:

git clone https://github.com/DataAccessioner/dafixity.git

Then execute the following command in the top-level directory:

    mvn clean package

A distributable zip file will be built and placed in the `target/`
subdirectory, with the name `dafixity-<version>-dist.zip`.

For testing purposes, you may run the created executable jar in 
the `target/` folder directly:

    dafixity-<version>.jar

## Install

Unzip the distribution zip file in the location of your choice.
All dependencies are included within the package.

## Run

Run `dafixity` in the `dafixity-<version>` folder:

    $ cd /path/to/dafixity-<version>
    $ ./dafixity -r /path/to/da/report -d /path/to/top/of/accession/tree

    usage: dafixity [options]
        -r,--report <REPORT>         Path to Data Accessioner report file
        -d,--directory <DIRECTORY>   Parent directory for accession tree in the
                                     report
        -h,--help                    This help message

The logs are written to `logs/dafixity-<YYYYMMDDHHmmss>.log`;  the 
report is written to `logs/dafixity-report-<YYYYMMDDHHmmss>.csv`, 
where *YYYYMMDDHHmmss* is the date-time stamp of the beginning of 
the run.

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request

## Credits

[Scott Prater](https://github.com/sprater)

## License

Copyright © 2017 by Scott Prater.

DAFixity is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

DAFixity is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
