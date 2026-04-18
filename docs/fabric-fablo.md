# Hyperledger Fabric Dev Network with Fablo

A local Hyperledger Fabric development network managed by [Fablo](https://github.com/hyperledger-labs/fablo).
The default setup runs one channel (`my-channel1`) with two peers in `Org1` and the `assignment-name-placeholder` chaincode deployed.

> [!TIP]
> Use [WSL](https://learn.microsoft.com/en-us/windows/wsl/install) if working on Windows.


## Prerequisites

- Docker (with Compose)
- Node.js / npm (for chaincode development)


## Quick start

```shell
/fablo up
```

This generates crypto material, starts all Docker containers, creates the channel, and deploys the chaincode.
Tear down the network (keeping generated artifacts) with:

```shell
./fablo stop
```

Or destroy everything including crypto material:

```shell
./fablo prune
```


## Network topology

| Component | Value |
|-----------|-------|
| Fabric version | 3.1.0 |
| Orderer org | `orderer.example.com` — 2-node BFT orderer group |
| Peer org | `org1.example.com` — 2 peers (`peer0`, `peer1`) |
| Channel | `my-channel1` |
| Chaincode | `assignment-name-placeholder` (Java) |

Adjust `fablo-config.json` to change the topology or swap chaincodes.


## Interacting with the network — `scripts/cc.sh`

`scripts/cc.sh` is a convenience wrapper around the `peer chaincode` CLI.
It runs inside the `cli.org1.example.com` Docker container, so no local `peer` binary is required.

### Get help

```shell
./scripts/cc.sh -h
```

### Examples

```console
# Store a value
$ ./scripts/cc.sh assignment-name-placeholder invoke put mykey hello

# Read it back
$ ./scripts/cc.sh assignment-name-placeholder query get mykey

# Same, but print the request and response
$ ./scripts/cc.sh -v assignment-name-placeholder query get mykey
```

Arguments that contain spaces or special characters are JSON-escaped automatically.
