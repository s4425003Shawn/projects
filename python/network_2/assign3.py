import struct
import sys
import threading
import socket


def ipv4(addr):
    return '.'.join(map(str, addr))


def unpack_packet(pack, payload_len):
    tuple = struct.unpack("!BBHHHBBH4s4s" + str(payload_len) + 's', pack)

    return tuple


def unpack_fragment(pack, payload_len):
    tuple = struct.unpack("!HHBH4s" + str(payload_len) + 's', pack)

    return tuple


def receiving(server_port):
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server_socket.bind(('', server_port))
    fragment = []
    fragment2 = []
    while True:
        message, client_address = server_socket.recvfrom(1500)

        data = unpack_packet(message, len(message) - 20)
        if data[4] == 0:
            if data[6] == 0:
                print('\b\b', end='')
                sys.stdout.flush()

                print("Message received from " + ipv4(data[8]) + ': "' + data[10].decode() + '"')
                sys.stdout.flush()
                print(">", end=' ')
                sys.stdout.flush()
            else:
                print('\b\b', end='')
                sys.stdout.flush()

                print("Message received from " + ipv4(data[8]) + ' with protocol ' + "0x{:02x}".format(data[6]))
                sys.stdout.flush()
                print(">", end=' ')
                sys.stdout.flush()
        else:
            if data[6] == 0:
                offset = (data[4] & 0b0001111111111111) * 8
                fragment_data = Fragment(data[3], len(data[10]), data[4] >> 13, offset, ipv4(data[8]), data[10]).pack()
                tuple = unpack_fragment(fragment_data, len(data[10].decode()))
                fragment.append(tuple)

                print_string = ""
                offset = []
                total_length = 0
                frag_dic = {}

                for i in fragment:

                    if data[3] == i[0] and ipv4(data[8]) == ipv4(i[4]):

                        print_string += i[5].decode()
                        if i[2] == 0:
                            total_length = i[1] + i[3]

                if total_length != 0:
                    if total_length == len(print_string):
                        if total_length == len(print_string):
                            print_string = ''
                            for g in fragment:
                                if data[3] == g[0] and ipv4(data[8]) == ipv4(g[4]):
                                    frag_dic[g[3]] = g[5].decode()
                                    offset.append(int(g[3]))
                            offset.sort()

                            for h in offset:
                                print_string += frag_dic[h]
                            fragment.clear()
                            print('\b\b', end='')
                            sys.stdout.flush()
                            print("Message received from " + ipv4(data[8]) + ': "' + print_string + '"')
                            sys.stdout.flush()
                            print(">", end=' ')
                            sys.stdout.flush()
            else:
                offset = (data[4] & 0b0001111111111111)*8
                fragment_data = Fragment(data[3], len(data[10]), data[4] >> 13, offset, ipv4(data[8]), data[10]).pack()
                tuple = unpack_fragment(fragment_data, len(data[10].decode()))
                fragment2.append(tuple)

                print_string = ""
                total_length = 0


                for i in fragment2:

                    if data[3] == i[0] and ipv4(data[8]) == ipv4(i[4]):

                        print_string += i[5].decode()
                        if i[2] == 0:
                            total_length = i[1] + i[3]

                if total_length != 0:
                    if total_length == len(print_string):
                        if total_length == len(print_string):
                            fragment2.clear()
                            print('\b\b', end='')
                            sys.stdout.flush()
                            print("Message received from " + ipv4(data[8]) + ' with protocol ' + "0x{:02x}".format(data[6]))
                            sys.stdout.flush()
                            print(">", end=' ')
                            sys.stdout.flush()
            # print (u[0])   //id
            # print(u[1])    //payload_length
            # print(u[2])    //mf
            # print(u[3])    //offset
            # print(ipv4(u[4]))   //ip_source_address
            # print(u[5].decode())   //payload


class Fragment(object):
    def __init__(self, id, payload_length, mf, offset, ip_saddr, payload):
        self.id = id
        self.payload_length = payload_length
        self.mf = mf
        self.offset = offset
        self.ip_saddr = socket.inet_aton(ip_saddr)
        self.payload = payload

    def pack(self):
        data_length = len(self.payload)
        raw = struct.pack('!HHBH4s' + str(data_length) + 's',
                          self.id,
                          self.payload_length,
                          self.mf,
                          self.offset,
                          self.ip_saddr,
                          self.payload,
                          )
        return raw


class IPPacket(object):
    def __init__(self, ip_idf, mf, offset, ip_saddr, ip_daddr, payload):
        self.ip_ver = (4 << 4) + 5
        self.ip_dfc = 0
        self.ip_tol = len(payload) + 20
        self.ip_idf = ip_idf
        self.ip_flg = (0 << 15) + (0 << 14) + (mf << 13) + offset

        self.ip_ttl = 65
        self.ip_proto = 0
        self.ip_chk = 0
        self.ip_saddr = socket.inet_aton(ip_saddr)
        self.ip_daddr = socket.inet_aton(ip_daddr)
        self.payload = payload

    def pack(self):
        data_length = len(self.payload)
        raw = struct.pack('!BBHHHBBH4s4s' + str(data_length) + 's',
                          self.ip_ver,  # IP Version
                          self.ip_dfc,  # Differentiate Service Feild
                          self.ip_tol,  # Total Length
                          self.ip_idf,  # Identification
                          self.ip_flg,  # Flags
                          self.ip_ttl,  # Time to leave
                          self.ip_proto,  # protocol
                          self.ip_chk,  # Checksum
                          self.ip_saddr,  # Source IP
                          self.ip_daddr,  # Destination IP
                          self.payload
                          )
        return raw


def ip_to_binary(ip):
    octet_list_int = ip.split(".")
    octet_list_bin = [format(int(i), '08b') for i in octet_list_int]
    binary = ("").join(octet_list_bin)
    return binary


def get_addr_network(address, net_size):
    ip_bin = ip_to_binary(address)
    network = ip_bin[0:32 - (32 - net_size)]
    return network


def ip_in_prefix(ip_address, prefix):
    [prefix_address, net_size] = prefix.split("/")
    net_size = int(net_size)
    prefix_network = get_addr_network(prefix_address, net_size)
    ip_network = get_addr_network(ip_address, net_size)
    return ip_network == prefix_network


def main():
    clientSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    host_subnet = sys.argv[1]
    host_address = host_subnet.split('/')[0]
    host_ll_addr = sys.argv[2]
    mtu = 1500
    identification = 0
    ip_addr = ''
    arp_table = {}

    t1 = threading.Thread(target=receiving, args=(int(host_ll_addr),), daemon=True)
    t1.start()

    while True:
        print(">", end=" ")
        sys.stdout.flush()

        x = input()
        if x.startswith('gw set'):
            ip_addr = x.split(' ')[2]
        elif x.startswith('gw get'):
            if ip_addr != '':
                print(ip_addr)
                sys.stdout.flush()
            else:
                print("None")
                sys.stdout.flush()
        elif x.startswith('arp set'):
            addr = x.split(' ')[2]
            port = x.split(' ')[3]

            arp_table[addr] = port

        elif x.startswith('arp get'):
            print(arp_table.get(x.split(' ')[2], None))
            sys.stdout.flush()

        elif x == "exit":
            clientSocket.close()
            exit()
        elif x.startswith("msg"):
            send_address = x.split(' ')[1]
            data = x.split(' ')[2]
            send_data = data[1:len(data) - 1]
            packet_len = len(send_data) + 20
            if ip_in_prefix(send_address, host_subnet):
                if arp_table.get(send_address, None) is not None:
                    if packet_len <= mtu:

                        packet = IPPacket(identification, 0, 0, host_address, send_address, send_data.encode()).pack()

                        clientSocket.sendto(packet, ('127.0.0.1', int(arp_table.get(send_address, None))))
                        identification += 1
                    else:
                        offset = 0
                        while packet_len > mtu:
                            packet = IPPacket(identification, 1, offset, host_address, send_address,
                                              send_data[offset:offset + mtu - 20].encode()).pack()
                            clientSocket.sendto(packet, ('127.0.0.1', int(arp_table.get(send_address, None))))
                            offset += (mtu - 20)/8
                            packet_len -= mtu - 20

                        packet = IPPacket(identification, 0, offset, host_address, send_address,
                                          send_data[offset:].encode()).pack()
                        clientSocket.sendto(packet, ('127.0.0.1', int(arp_table.get(send_address, None))))
                        identification += 1


                else:
                    print("No ARP entry found")
                    sys.stdout.flush()
            else:
                if ip_addr == '':
                    print("No gateway found")
                    sys.stdout.flush()
                else:
                    if arp_table.get(ip_addr, None) is not None:
                        if packet_len <= mtu:
                            packet = IPPacket(identification, 0, 0, host_address, send_address, send_data.encode()).pack()
                            clientSocket.sendto(packet, ('127.0.0.1', int(arp_table.get(ip_addr, None))))
                            identification += 1
                        else:
                            offset = 0
                            while packet_len > mtu:
                                packet = IPPacket(identification, 1, offset, host_address, send_address,
                                                  send_data[offset*8:offset*8 + mtu - 20].encode()).pack()
                                clientSocket.sendto(packet, ('127.0.0.1', int(arp_table.get(ip_addr, None))))
                                offset += int((mtu - 20)/8)
                                
                                packet_len -= mtu - 20

                            packet = IPPacket(identification, 0, offset, host_address, send_address,
                                              send_data[offset*8:].encode()).pack()
                            clientSocket.sendto(packet, ('127.0.0.1', int(arp_table.get(ip_addr, None))))
                            identification += 1
                    else:
                        print("No ARP entry found")
                        sys.stdout.flush()
        elif x.startswith("mtu set"):
            mtu = int(x.split(' ')[2])
        elif x.startswith("mtu get"):
            print(mtu)
            sys.stdout.flush()


if __name__ == '__main__':
    main()
